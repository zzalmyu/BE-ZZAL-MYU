package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.domain.entity.ImageLike;
import com.prgrms.zzalmyu.domain.image.domain.entity.ImageTag;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import com.prgrms.zzalmyu.domain.user.domain.enums.SocialType;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ImageTagRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ImageTagRepository imageTagRepository;
    @Autowired
    ImageChatCountRepository imageChatCountRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ImageLikeRepository imageLikeRepository;

    Long userId;
    Long userId2;
    User user;
    User user1;
    Tag tag1;
    Tag tag2;
    Tag tag3;
    Tag tag4;
    @BeforeEach
    void init() {
        user = User.builder()
                .email("sss9073@naver.com")
                .role(Role.USER)
                .socialId("socialId")
                .socialType(SocialType.GOOGLE)
                .nickname("nickname").build();
        userRepository.save(user);
        user1 = User.builder()
                .email("user1@naver.com")
                .role(Role.USER)
                .socialId("socialId")
                .socialType(SocialType.GOOGLE)
                .nickname("nickname").build();
        userRepository.save(user);
        userRepository.save(user1);
        userId = user.getId();
        userId2 = user1.getId();
        tag1 = Tag.from("토끼");
        tag2 = Tag.from("고양이");
        tag3 = Tag.from("강아지");
        tag4 = Tag.from("안유진");
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        tagRepository.save(tag4);

    }

    @DisplayName("업로드한 이미지에서 태그 id 리스트에 다 일치하는 이미지를 반환한다.")
    @Test
    void findUploadImagesByUserIdAndTagIdListTest(){
        //Given
        Image image1 = Image.builder()
                .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                .userId(userId)
                .s3Key("s3key")
                .title("title1")
                .path("path")
                .build();
        Image image2 = Image.builder()
                .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                .userId(userId)
                .s3Key("s3key")
                .title("title2")
                .path("path")
                .build();
        Image image3 = Image.builder()
                .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                .userId(userId2)
                .s3Key("s3key")
                .title("title3")
                .path("path")
                .build();
        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);
        ImageTag imageTag1 = ImageTag.builder()
                .tag(tag1)
                .image(image1)
                .build();
        ImageTag imageTag2 = ImageTag.builder()
                .tag(tag2)
                .image(image1)
                .build();
        // 같은 유저이지만 태그가 다름
        ImageTag imageTag3 = ImageTag.builder()
                .tag(tag2)
                .image(image2)
                .build();
        ImageTag imageTag4 = ImageTag.builder()
                .tag(tag3)
                .image(image2)
                .build();
        // 태그는 같지만 다른 유저
        ImageTag imageTag5 = ImageTag.builder()
                .tag(tag1)
                .image(image3)
                .build();
        ImageTag imageTag6 = ImageTag.builder()
                .tag(tag2)
                .image(image3)
                .build();
        imageTagRepository.save(imageTag1);
        imageTagRepository.save(imageTag2);
        imageTagRepository.save(imageTag3);
        imageTagRepository.save(imageTag4);
        imageTagRepository.save(imageTag5);
        imageTagRepository.save(imageTag6);

        //When
        List<Long> tagIdList = List.of(tag1.getId(), tag2.getId());
        List<ImageResponseDto> imageResultList = imageTagRepository.findUploadImagesByUserIdAndTagIdList(userId, tagIdList, PageRequest.of(0, 15));
        //Then
        assertThat(imageResultList).hasSize(1);
        assertThat(imageResultList.get(0).getTitle()).isEqualTo(image1.getTitle());
     }

     @DisplayName("좋아요한 이미지에서 태그 id 리스트에 다 일치하는 이미지를 반환한다.")
     @Test
     void findLikeImagesByUserIdAndTagIdListTest(){
         //Given
         Image image1 = Image.builder()
                 .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                 .userId(userId)
                 .s3Key("s3key")
                 .title("title1")
                 .path("path")
                 .build();
         Image image2 = Image.builder()
                 .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                 .userId(userId)
                 .s3Key("s3key")
                 .title("title2")
                 .path("path")
                 .build();
         Image image3 = Image.builder()
                 .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                 .userId(userId2)
                 .s3Key("s3key")
                 .title("title3")
                 .path("path")
                 .build();
         imageRepository.save(image1);
         imageRepository.save(image2);
         imageRepository.save(image3);
         ImageTag imageTag1 = ImageTag.builder()
                 .tag(tag1)
                 .image(image1)
                 .build();
         ImageTag imageTag2 = ImageTag.builder()
                 .tag(tag2)
                 .image(image1)
                 .build();
         // 같은 유저이지만 태그가 다름
         ImageTag imageTag3 = ImageTag.builder()
                 .tag(tag2)
                 .image(image2)
                 .build();
         ImageTag imageTag4 = ImageTag.builder()
                 .tag(tag3)
                 .image(image2)
                 .build();
         // 태그는 같지만 다른 유저
         ImageTag imageTag5 = ImageTag.builder()
                 .tag(tag1)
                 .image(image3)
                 .build();
         ImageTag imageTag6 = ImageTag.builder()
                 .tag(tag2)
                 .image(image3)
                 .build();
         imageTagRepository.save(imageTag1);
         imageTagRepository.save(imageTag2);
         imageTagRepository.save(imageTag3);
         imageTagRepository.save(imageTag4);
         imageTagRepository.save(imageTag5);
         imageTagRepository.save(imageTag6);

         ImageLike imageLike = ImageLike
                 .builder()
                 .user(user1)
                 .image(image1)
                 .build();
         imageLikeRepository.save(imageLike);
         //When
         List<Long> tagIdList = List.of(tag1.getId(), tag2.getId());
         List<Image> likeImageResult = imageTagRepository.findLikeImagesByUserIdAndTagIdList(user1.getId(), tagIdList, PageRequest.of(0, 15));
         //Then
         assertThat(likeImageResult).hasSize(1);
         assertThat(likeImageResult.get(0).getTitle()).isEqualTo(image1.getTitle());
      }

}