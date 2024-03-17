package com.prgrms.zzalmyu.domain.tag.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import com.prgrms.zzalmyu.domain.tag.exception.TagException;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagUserRepository;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagMeResponseDto;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagUserRepository tagUserRepository;
    private static final int RANK_NUM = 5;

    @Override
    public List<TagResponseDto> getTopTagsFromUsersUsed() {
        return tagRepository.getTopTagsFromUserUsed(RANK_NUM);
    }

    @Override
    public List<TagMeResponseDto> getTopTagsFromLikeImages(User user) {
        return tagRepository.getTopTagsFromLikedImages(user.getId(), RANK_NUM);
    }

    @Override
    public List<TagMeResponseDto> getTopTagsFromUploadImages(User user) {
        return tagRepository.getTopTagsFromUploadImages(user.getId(), RANK_NUM);
    }

    @Override
    public List<TagResponseDto> getRecommendationTags(User user) {
        return tagRepository.getRecommendationTags(user);
    }

    @Override
    public TagResponseDto createTag(User user, String tagName) {
        if (tagRepository.existsByName(tagName)) {
            throw new TagException(ErrorCode.TAG_ALREADY_EXIST_ERROR);
        }
        String splitTagName = splitTagName(tagName);
        Tag newTag = Tag.from(tagName, splitTagName);
        tagRepository.save(newTag);
        TagUser tagUser = TagUser.builder()
                .tagId(newTag.getId())
                .userId(user.getId())
                .build();
        tagUserRepository.save(tagUser);
        return new TagResponseDto(newTag);
    }

    @Override
    public List<TagResponseDto> searchTag(String keyword) {
        // keyword가 빈 값일 시 그냥 빈 리스트를 반환한다.
        if (keyword.isBlank()) {
            return new ArrayList<>();
        }
        String splitTagName = splitTagName(keyword);
        return tagRepository.searchTagForAutoSearchName(splitTagName);
    }

    @Override
    public List<TagResponseDto> searchTagFromLikeImages(User user, String keyword) {
        // keyword가 빈 값일 시 그냥 빈 리스트를 반환한다.
        if (keyword.isBlank()) {
            return new ArrayList<>();
        }
        String splitTagName = splitTagName(keyword);
        return tagRepository.searchTagForAutoSearchNameFromLikeImages(user.getId(), splitTagName);
    }

    @Override
    public List<TagResponseDto> searchTagFromUploadImages(User user, String keyword) {
        // keyword가 빈 값일 시 그냥 빈 리스트를 반환한다.
        if (keyword.isBlank()) {
            return new ArrayList<>();
        }
        String splitTagName = splitTagName(keyword);
        return tagRepository.searchTagForAutoSearchNameFromUploadImages(user.getId(), splitTagName);
    }

    @Override
    public TagResponseDto increaseTagCount(User user, String tagName) {
        Tag tag = tagRepository.findByName(tagName).orElseThrow(() -> new TagException(ErrorCode.TAG_NOT_FOUND_ERROR));
        Long tagId = tag.getId();
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
        TagUser tagUser = tagUserRepository.findByTagIdAndUserId(tagId, user.getId()).orElseThrow(() -> new TagException(ErrorCode.TAG_NOT_FOUND_ERROR));
        return TagResponseDto.from(tag, tagUser);
    }

    @Override
    public String splitTagName(String input) {
        char[] onsets = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
        char[] nuclears = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
        char[] codas = {'_', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char syllable = input.charAt(i);
            if (syllable >= '가' && syllable <= '힣') {
                int syllableNumber = syllable - '가';
                int choIndex = syllableNumber / (21 * 28);
                int jungIndex = (syllableNumber % (21 * 28)) / 28;
                int jongIndex = (syllableNumber % (21 * 28)) % 28;

                resultBuilder.append(onsets[choIndex]);
                resultBuilder.append(nuclears[jungIndex]);
                if (jongIndex != 0) {
                    resultBuilder.append(codas[jongIndex]);
                }
            } else {
                resultBuilder.append(syllable);
            }
        }
        return resultBuilder.toString();
    }
}
