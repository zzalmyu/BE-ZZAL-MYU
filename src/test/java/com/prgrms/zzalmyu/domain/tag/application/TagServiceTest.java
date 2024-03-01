package com.prgrms.zzalmyu.domain.tag.application;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import com.prgrms.zzalmyu.domain.chat.infrastructure.ImageChatCountRepository;
import com.prgrms.zzalmyu.domain.image.application.ImageService;
import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.domain.entity.ImageTag;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageTagRepository;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagUserRepository;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    TagService tagService;

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageTagRepository imageTagRepository;

    @Autowired
    ImageChatCountRepository imageChatCountRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TagUserRepository tagUserRepository;

    @Autowired
    UserRepository userRepository;

    User user1 = User.builder()
            .email("user1@naver.com")
            .nickname("유저1")
            .role(Role.USER)
            .build();

    User user2 = User.builder()
            .email("user2@naver.com")
            .nickname("유저2")
            .role(Role.USER)
            .build();

    Tag tag1 = Tag.from("tag1");
    Tag tag2 = Tag.from("tag2");
    Tag tag3 = Tag.from("tag3");
    Tag tag4 = Tag.from("tag4");
    Tag tag5 = Tag.from("tag5");
    Tag tag6 = Tag.from("tag6");
    Tag tag7 = Tag.from("tag7");
    List<Tag> tagList = List.of(tag1, tag2, tag3, tag4, tag5, tag6, tag7);


    @BeforeEach
    public void beforeEach() {
        userRepository.save(user1);
        userRepository.save(user2);
        tagRepository.saveAllAndFlush(tagList);
    }

    @AfterEach
    public void afterEach() {
        tagUserRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @DisplayName("유저들이 가장 많이 사용한 태그 리스트를 가져올 수 있다.(정책 상 현재 5개)")
    @Test
    void getTopTagsFromUserUsed() {
        //Given
        // user1은 숫자 순서대로 6,5,4,3,2,1,0 번 사용했다 가정
        for (int i = 6; i >= 0; i--) {
            Tag tag = tagList.get(6 - i);
            TagUser tagUser = TagUser.builder()
                    .tagId(tag.getId())
                    .userId(user1.getId())
                    .build();
            tagUserRepository.save(tagUser);
            for (int j = 0; j < i; j++) {
                tagUser.increaseCount();
            }
        }
        // user2는 숫자 순서대로 2,4,6,8,10,12,14 번 사용했다 가정
        for (int i = 2; i <= 14; i += 2) {
            Tag tag = tagList.get((i / 2) - 1);
            TagUser tagUser = TagUser.builder()
                    .tagId(tag.getId())
                    .userId(user2.getId())
                    .build();
            tagUserRepository.save(tagUser);
            for (int j = 0; j < i; j++) {
                tagUser.increaseCount();
            }
        }
        // 사용 sum은 순서대로 8, 9, 10, 11, 12, 13, 14
        // When
        List<TagResponseDto> responseDtoList = tagService.getTopTagsFromUsersUsed();
        //Then
        assertThat(responseDtoList).hasSizeLessThanOrEqualTo(5);
        assertThat(responseDtoList.stream().map(TagResponseDto::getTagId).toList())
                .containsExactlyElementsOf(List.of(tag7.getId(), tag6.getId(), tag5.getId(), tag4.getId(), tag3.getId()));
    }

    @Test
    void getTopTagsFromLikeImages() {

    }

    @Test
    void getTopTagsFromUploadImages() {

    }

    @Test
    void createTag() {
        String requestTagName = "요청태그이름";
        TagResponseDto responseDto = tagService.createTag(requestTagName);
        assertThat(responseDto.getTagName()).isEqualTo(requestTagName);
    }


    @DisplayName("유저가 좋아요한 사진들의 태그들로부터 초성/중성/종성을 활용한 검색이 가능하다.")
    @Test
    void searchTagFromLikeImages() {
        //Given
        Image image = Image.builder()
                .title("title1")
                .userId(user1.getId())
                .path("path")
                .s3Key("s3key")
                .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                .build();
        imageRepository.save(image);
        String tagName = "안유진";
        String splitTagName = tagService.splitTagName(tagName);
        Tag tag = Tag.from(tagName, splitTagName);
        tagRepository.save(tag);
        ImageTag imageTag = ImageTag.builder()
                .image(image)
                .tag(tag)
                .build();
        imageTagRepository.save(imageTag);
        TagUser tagUser = TagUser.builder()
                .userId(user1.getId())
                .tagId(tag.getId())
                .build();
        tagUserRepository.save(tagUser);
        imageService.likeImage(image.getId(), user2);
        //When
        List<TagResponseDto> tagResponseDtos = tagService.searchTagFromLikeImages(user2, "안ㅇ");
        //Then
        assertThat(tagResponseDtos.stream().map(TagResponseDto::getTagName)).containsExactlyInAnyOrder(tagName);
    }

    @DisplayName("유저가 업로드한 사진들의 태그들로부터 초성/중성/종성을 활용한 검색이 가능하다.")
    @Test
    void searchTagFromUploadImages() {
        //Given
        Image image = Image.builder()
                .title("title1")
                .userId(user1.getId())  // user1이 업로드
                .path("path")
                .s3Key("s3key")
                .imageChatCount(imageChatCountRepository.save(new ImageChatCount()))
                .build();
        imageRepository.save(image);
        String tagName = "안유진";
        String splitTagName = tagService.splitTagName(tagName);
        Tag tag = Tag.from(tagName, splitTagName);
        tagRepository.save(tag);
        ImageTag imageTag = ImageTag.builder()
                .image(image)
                .tag(tag)
                .build();
        imageTagRepository.save(imageTag);
        TagUser tagUser = TagUser.builder()
                .userId(user1.getId())
                .tagId(tag.getId())
                .build();
        tagUserRepository.save(tagUser);
        //When
        List<TagResponseDto> tagResponseDtos = tagService.searchTagFromUploadImages(user1, "안ㅇ");
        //Then
        assertThat(tagResponseDtos.stream().map(TagResponseDto::getTagName)).containsExactlyInAnyOrder(tagName);
    }

    @DisplayName("태그를 검색 시 가장 많이 사용한 상위 10개를 보여준다.")
    @Test
    void searchTagForAutoSearchLimit10() {
        //Given
        List<String> expectedTagNameList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String tagName = "tagName" + i;
            TagResponseDto tagResponseDto = tagService.createTag(tagName);
            TagUser tagUser = TagUser.builder()
                    .userId(user1.getId())
                    .tagId(tagResponseDto.getTagId())
                    .build();
            tagUserRepository.save(tagUser);
            // 인덱스에 따른 횟수 증가
            for (int j = 0; j < i; j++) {
                tagUser.increaseCount();
            }
            if (i >= 10) {
                expectedTagNameList.add(tagName);
            }
        }
        //When
        List<TagResponseDto> result = tagService.searchTag("tagNa");
        //Then
        assertThat(result).hasSizeLessThanOrEqualTo(10);
        assertThat(result.stream().map(TagResponseDto::getTagName).toList())
                .containsExactlyInAnyOrderElementsOf(expectedTagNameList);
    }
}