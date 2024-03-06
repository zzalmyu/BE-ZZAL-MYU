package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.infrastructure.ImageLikeRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagUserRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageMainService {

    private static final int MAX = 150;
    private static final int PER_COUNT = 15;
    private static final int BEST_IMAGE = 30;

    private TagUserRepository tagUserRepository;
    private ImageRepository imageRepository;
    private ImageTagRepository imageTagRepository;
    private ImageLikeRepository imageLikeRepository;
    private RedisTemplate<String, ImageResponseDto> imageRedisTemplate;
    private SetOperations<String, ImageResponseDto> setOperations;

    @Autowired
    public ImageMainService(TagUserRepository tagUserRepository, ImageRepository imageRepository, ImageLikeRepository imageLikeRepository, ImageTagRepository imageTagRepository, RedisTemplate<String, ImageResponseDto> imageRedisTemplate) {
        this.tagUserRepository = tagUserRepository;
        this.imageRepository = imageRepository;
        this.imageLikeRepository = imageLikeRepository;
        this.imageTagRepository = imageTagRepository;
        this.imageRedisTemplate = imageRedisTemplate;
        setOperations = this.imageRedisTemplate.opsForSet(); // redis set 자료구조 이용(초기화)
    }

    public List<ImageResponseDto> getTopUserUsedImage(Pageable pageable) {
        List<ImageResponseDto> list = imageRepository.getTopUserUsedImage(pageable);
        return list;
    }

    public List<ImageResponseDto> getRecommendedImage(User user, Pageable pageable) {

        if (pageable.getOffset() == 0) { //새로고침 하는 경우에는 반드시 새로 불러와야함.
            //해당 user에 대한 redis set 값 비우고 불러오기
            if (setOperations.getOperations().hasKey(user.getId().toString())) {
                setOperations.members(user.getId().toString()).clear();
            }
            addRecommendedImageNew(user);
            return getImageList(user);

        } else {
            /**
             * Redis에 값이 남아서 아직 불러올 수 있을 때는 여기서 처리
             */
            if (setOperations.size(user.getId().toString()) != 0) {
                return getImageList(user);
            }

            /**
             * Redis에 값이 없어서 새로 불러와야할 때
             */
            else {
                addRecommendedImageNew(user);
                return getImageList(user);
            }
        }
    }

    private List<ImageResponseDto> getImageList(User user) {
        //레디스에 저장된 내가 자주보는 태그 기반의 image
        return setOperations.pop(user.getId().toString(), PER_COUNT);
    }

    // setOperations에 새로운 Image 채워넣음
    private void addRecommendedImageNew(User user) {
        /**
         * Redis set에 값이 없을때 새로 이미지 불러와서 저장시켜놓기 (150개,변경 가능)
         */

        // 유저가 많이 사용한 태그 id와 count 를 얻어야 함
        List<TagUser> tagUsers = tagUserRepository.findByUserIdOrderByCountDesc(user.getId());
        //전체 사람들에게 인기 많은 image도 추가

        int totalCount = tagUsers.stream()
                .mapToInt(TagUser::getCount)
                .sum();

        tagUsers.stream()
                .filter(tagUser -> ((tagUser.getCount() * MAX) / totalCount) > 0)
                .forEach(tagUser -> {
                    //가져와야할 image개수
                    int limit = (tagUser.getCount() * MAX) / totalCount;

                    imageTagRepository.findImageByTagIdAndLimit(tagUser.getTagId(), limit)
                            .stream()
                            .forEach(image -> {
                                boolean present = imageLikeRepository.findByUserIdAndImageId(user.getId(), image.getId()).isPresent();
                                setOperations.add(user.getId().toString(),
                                        ImageResponseDto.getImageByLoginUser(image, present));
                            });
                });

        imageRepository.findTopImageLike(BEST_IMAGE)
                .forEach(image -> {
                    boolean present = imageLikeRepository.findByUserIdAndImageId(user.getId(), image.getId()).isPresent();
                    setOperations.add(user.getId().toString(),
                            ImageResponseDto.getImageByLoginUser(image, present));
                });

    }

}
