package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.domain.entity.ImageLike;
import com.prgrms.zzalmyu.domain.image.exception.ImageException;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageLikeRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageDetailResponse;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageLikeRepository imageLikeRepository;

    @Transactional(readOnly = true)
    @Override
    public ImageDetailResponse getImageDetail(Long imageId, User user) {
        Image image = getImage(imageId);
        List<Tag> tags = imageRepository.findTagsByImageId(imageId);
        boolean likeImage = isLikeImage(imageId, user.getId());
        return ImageDetailResponse.of(image, tags, likeImage);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AwsS3ResponseDto> getLikeImages(User user, Pageable pageable) {
        return imageRepository.findImageLikesByUserId(user.getId(),pageable)
                .stream()
                .map(AwsS3ResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AwsS3ResponseDto> getUploadImages(User user,Pageable pageable) {
        return imageRepository.findByUserId(user.getId(),pageable)
                .stream()
                .map(AwsS3ResponseDto::new)
                .toList();
    }

    @Override
    public void likeImage(Long imageId, User user) {
        Image image = getImage(imageId);
        if (isLikeImage(imageId, user.getId())) {
            throw new ImageException(ErrorCode.IMAGE_ALREADY_LIKE);
        }
        imageLikeRepository.save(ImageLike.builder()
                .image(image)
                .user(user)
                .build()
        );
    }

    @Override
    public void cancelLikeImage(Long imageId, User user) {
        Image image = getImage(imageId);
        ImageLike imageLike = imageLikeRepository.findByUserIdAndImageId(user.getId(), imageId)
                .orElseThrow(() -> new ImageException(ErrorCode.IMAGE_ALREADY_LIKE_CANCLE));
        imageLikeRepository.delete(imageLike);
    }

    @Override
    public List<AwsS3ResponseDto> getAllImages(Pageable pageable) {
        return imageRepository.findAll(pageable)
                .stream()
                .map(AwsS3ResponseDto::new)
                .toList();
    }

    private Image getImage(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new ImageException(ErrorCode.IMAGE_NOT_FOUND_ERROR));
    }

    private boolean isLikeImage(Long imageId, Long userId) {
        return imageLikeRepository.findByUserIdAndImageId(userId, imageId).isPresent();
    }
}
