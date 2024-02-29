package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.req.TagSearchRequestDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagUserRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageSearchServiceImpl implements ImageSearchService {
    private final ImageTagRepository imageTagRepository;
    private final TagUserRepository tagUserRepository;

    @Override
    public List<AwsS3ResponseDto> searchLikeImages(User user, TagSearchRequestDto dto) {
        List<Image> imageList = imageTagRepository.findLikeImagesByUserIdAndTagIdList(user.getId(), dto.getTagIdList());
        saveTagUserMapping(user, dto.getNewTagId());
        return convertListToResponseDtoList(imageList);
    }

    @Override
    public List<AwsS3ResponseDto> searchUploadImages(User user, TagSearchRequestDto dto) {
        List<Image> imageList = imageTagRepository.findUploadImagesByUserIdAndTagIdList(user.getId(), dto.getTagIdList());
        saveTagUserMapping(user, dto.getNewTagId());
        return convertListToResponseDtoList(imageList);
    }

    private List<AwsS3ResponseDto> convertListToResponseDtoList(List<Image> imageList) {
        return imageList.stream()
                .map(AwsS3ResponseDto::new)
                .toList();
    }

    private void saveTagUserMapping(User user, Long tagId) {
        Optional<TagUser> optionalTagUser = tagUserRepository.findByTagIdAndUserId(tagId, user.getId());
        optionalTagUser.ifPresentOrElse(
                TagUser::increaseCount,
                () -> {
                    TagUser newTagUser = TagUser.builder()
                            .userId(user.getId())
                            .tagId(tagId)
                            .build();
                    tagUserRepository.save(newTagUser);
                }
        );
    }
}
