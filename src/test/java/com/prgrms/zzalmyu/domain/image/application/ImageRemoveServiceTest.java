package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.exception.ImageException;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageLikeRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3RequestDto;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageRemoveServiceTest {

    @InjectMocks
    private ImageRemoveService imageRemoveService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageLikeRepository imageLikeRepository;
    @Mock
    private ImageTagRepository imageTagRepository;
    @Mock
    private AwsS3Service awsS3Service;
    private static String path = "zzalmyu.site/won05121@naver.com/1";
    private static String tagName = "태그1";
    private Image image;
    private User user;

    @BeforeEach
    void init() {
        user = User.builder().email("won05121@naver.com").nickname("kim").role(Role.USER).build();
        ReflectionTestUtils.setField(user, "id", 1L);

        image = Image.builder()
                .s3Key("key").path(path).imageChatCount(new ImageChatCount()).build();
        ReflectionTestUtils.setField(image, "id", 1L);
    }

    @Test
    @DisplayName("짤을 업로드한 사용자가 직접 짤을 삭제하는데 성공한다. ")
    public void deleteUploadImagesSuccess() {
        //given
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        ReflectionTestUtils.setField(image,"userId",user.getId());
        //when
        imageRemoveService.deleteUploadImages(user, 1L);

        //then
        verify(awsS3Service).remove(any());
        verify(imageRepository).delete(any());
        verify(imageLikeRepository).deleteImageLikeByImageId(any());
        verify(imageTagRepository).findImageTagIdsByImageId(any());
        verify(imageTagRepository).deleteAllByIdInBatch(any());

    }


    @Test
    @DisplayName("짤을 업로드하지 않은 사용자가 직접 짤을 삭제하면 예외가 발생한다. ")
    public void deleteUploadImagesFail() {
        //given
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        ReflectionTestUtils.setField(image,"userId",12345L);

        //when&&then
        Assertions.assertThatThrownBy(() ->
                        imageRemoveService.deleteUploadImages(user, 1L))
                .isInstanceOf(ImageException.class);

    }


    @Test
    @DisplayName("관리자가 신고가 3번 이상인 짤을 삭제하는데 성공한다. ")
    public void deleteReportImageSuccess() {
        //given
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        //when
        imageRemoveService.deleteReportImage(image.getId());

        //then
        verify(awsS3Service).remove(any());
        verify(imageRepository).delete(any());
        verify(imageLikeRepository).deleteImageLikeByImageId(any());
        verify(imageTagRepository).findImageTagIdsByImageId(any());
        verify(imageTagRepository).deleteAllByIdInBatch(any());

    }
}
