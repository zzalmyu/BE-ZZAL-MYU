package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.domain.entity.ImageLike;
import com.prgrms.zzalmyu.domain.image.exception.ImageException;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageLikeRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageDetailResponse;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageLikeRepository imageLikeRepository;
    @InjectMocks
    private ImageServiceImpl imageService;

    private static String path = "zzalmyu.site/won05121@naver.com/1";
    private static String tagName = "태그1";
    private Image image;
    private Tag tag;
    private User user;

    @BeforeEach
    void init(){
        image = Image.builder()
                .s3Key("key").path(path).imageChatCount(new ImageChatCount()).build();
        tag = Tag.from(tagName);
        user = User.builder().email("won05121@naver.com").nickname("kim").role(Role.USER).build();

    }
    @Test
    @DisplayName("존재하지 않는 짤을 상세보기하면 예외가 발생한다.")
    void getImageDetailFail(){
        //given
        when(imageRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        //when&&then
        Assertions.assertThatThrownBy(()->imageService.getImageDetail(1L,user))
                .isInstanceOf(ImageException.class);
    }

    @Test
    @DisplayName("짤 상세보기를 성공한다.")
    void getImageDetailSuccess() {
        //given
        List<Tag> tags = List.of(tag);
        when(imageRepository.findById(any())).thenReturn(Optional.of(image));
        when(imageRepository.findTagsByImageId(any())).thenReturn(tags);
        when(imageLikeRepository.findByUserIdAndImageId(any(),any())).
                thenReturn(Optional.of(ImageLike.builder().build()));

        //when
        ImageDetailResponse imageDetail = imageService.getImageDetail(1L, user);

        //then
        Assertions.assertThat(imageDetail.getImgUrl()).isEqualTo(path);
        Assertions.assertThat(imageDetail.getTags().get(0).getTagName()).isEqualTo(tagName);
        Assertions.assertThat(imageDetail.isImageLikeYn()).isTrue();

    }

    @Test
    @DisplayName("좋아요한 짤 리스트를 불러오는데 성공한다.")
    void getLikeImagesSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        ReflectionTestUtils.setField(image,"id",1L);
        List<Image>images =List.of(image);
        when(imageRepository.findImageLikesByUserId(any(),any())).thenReturn(images);

        //when
        List<ImageResponseDto> likeImages = imageService.getLikeImages(user,pageable);

        //then
        Assertions.assertThat(likeImages.get(0).getImageId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("업로드한 짤 리스트를 불러오는데 성공한다.")
    void getUploadImagesSuccess() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        ReflectionTestUtils.setField(image,"id",1L);
        ImageResponseDto imageResponseDto = ImageResponseDto.of(image, true);
        List<ImageResponseDto> imageResponseDto1 = List.of(imageResponseDto);
        when(imageRepository.findByUserId(any(),any())).thenReturn(imageResponseDto1);

        //when
        List<ImageResponseDto> likeImages = imageService.getUploadImages(user,pageable);

        //then
        Assertions.assertThat(imageResponseDto1.get(0).getImageId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("짤을 좋아요하는데 성공한다.") //likeImage는 메서드 자체를 수정 필요
    void likeImageSuccess() {
        //given
        //when
        //then
    }
}
