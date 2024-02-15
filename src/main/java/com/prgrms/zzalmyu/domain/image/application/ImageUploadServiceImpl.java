package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.domain.entity.AwsS3;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.domain.entity.ImageTag;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import com.prgrms.zzalmyu.domain.tag.exception.TagException;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagUserRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageUploadServiceImpl implements ImageUploadService {
    private final AwsS3Service awsS3Service;
    private final ImageChatCountRepository imageChatCountRepository;
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final ImageTagRepository imageTagRepository;
    private final TagUserRepository tagUserRepository;

    @Override
    public AwsS3ResponseDto uploadImage(User user, MultipartFile multipartFile, List<Long> tagIdList) throws IOException {
        AwsS3 awsS3 = awsS3Service.upload(user, multipartFile);
        Image image = saveImage(user, awsS3);
        saveImageTagMapping(tagIdList, image);
        saveTagUserMapping(user, tagIdList);
        return awsS3.convertResponseDto(image.getId());
    }

    private Image saveImage(User user, AwsS3 awsS3) {
        Image image = Image.builder()
                .s3Key(awsS3.getKey())
                .path(awsS3.getPath())
                .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                .userId(user.getId())
                .build();
        imageRepository.save(image);
        return image;
    }

    private void saveImageTagMapping(List<Long> tagIdList, Image image) {
        tagIdList.stream()
                .map(tagId -> tagRepository.findById(tagId).orElseThrow(() -> new TagException(ErrorCode.TAG_NOT_FOUND_ERROR)))
                .map(tag -> ImageTag.builder()
                        .tag(tag)
                        .image(image)
                        .build())
                .forEach(imageTagRepository::save);
    }

    private void saveTagUserMapping(User user, List<Long> tagIdList) {
        tagIdList.stream()
                .forEach(tagId -> {
                    Optional<TagUser> optionalTagUser = tagUserRepository.findByTagId(tagId);
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
                });
    }
}
