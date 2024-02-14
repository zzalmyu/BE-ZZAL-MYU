package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.exception.ImageException;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageLikeRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageRemoveService {
    private final ImageRepository imageRepository;
    private final ImageLikeRepository imageLikeRepository;
    private final ImageTagRepository imageTagRepository;
    private final ImageChatCountRepository imageChatCountRepository;
    private final AwsS3Service awsS3Service;

    /**
     * 업로드한 사진 삭제 인자 : 사진 id (단건) 로직: 해당 사진이 해당 유저가 업로드한 사진인지 체크
     */
    public void deleteUploadImages(User user, AwsS3RequestDto awsS3RequestDto) {

        Image image = getImage(awsS3RequestDto.getImageId());
        if (!image.getUserId().equals(user.getId())) {
            throw new ImageException(ErrorCode.IMAGE_ONLY_UPLOAD_USER_DELETE);
        }
        deleteImage(image);

    }

    /**
     * 신고가 3번 누적된 사진 삭제 hard delete 사용 관리자 권한 여부 체크
     */
    @Secured("ROLE_ADMIN")
    public void deleteReportImage(Long imageId) {
        Image image = getImage(imageId);
        deleteImage(image);
    }

    private Image getImage(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new ImageException(ErrorCode.IMAGE_NOT_FOUND_ERROR));
    }

    private void deleteImage(Image image) {
        awsS3Service.remove(image); //aws에서 이미지 삭제
        imageRepository.delete(image); // 이미지 삭제
        imageLikeRepository.deleteImageLikeByImageId(image.getId());// 이미지 좋아요 삭제

        List<Long> imageTags = imageTagRepository.findImageTagIdsByImageId(image.getId());//이미지 태그 다 불러오기
        imageTagRepository.deleteAllByIdInBatch(imageTags);// 이미지 태그 배치 삭제
    }

}
