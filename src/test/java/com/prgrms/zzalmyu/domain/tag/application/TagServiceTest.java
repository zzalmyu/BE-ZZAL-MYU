package com.prgrms.zzalmyu.domain.tag.application;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired
    TagService tagService;

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
    void getTopTagsFromUserUsed(){
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
        List<TagResponseDto> responseDtoList = tagService.getTopTagsFromUserUsed(user1);
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
        TagResponseDto responseDto = tagService.createTag(user1, requestTagName);
        assertThat(responseDto.getTagName()).isEqualTo(requestTagName);
    }
}