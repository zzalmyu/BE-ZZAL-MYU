package com.prgrms.zzalmyu.domain.tag.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.tag.exception.TagException;
import com.prgrms.zzalmyu.domain.tag.infrastructure.TagRepository;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private static final int RANK_NUM = 5;
    @Override
    public List<TagResponseDto> getTopTagsFromUsersUsed() {
        return tagRepository.getTopTagsFromUserUsed(RANK_NUM);
    }

    @Override
    public List<TagResponseDto> getTopTagsFromLikeImages(User user) {
        return tagRepository.getTopTagsFromLikedImages(user.getId(), RANK_NUM);
    }

    @Override
    public List<TagResponseDto> getTopTagsFromUploadImages(User user) {
        return tagRepository.getTopTagsFromUploadImages(user.getId(), RANK_NUM);
    }

    @Override
    public TagResponseDto createTag(String tagName) {
        if (tagRepository.existsByName(tagName)) {
            throw new TagException(ErrorCode.TAG_ALREADY_EXIST_ERROR);
        }
        String splitTagName = splitTagName(tagName);
        Tag newTag = Tag.from(tagName, splitTagName);
        tagRepository.save(newTag);
        return new TagResponseDto(newTag);
    }

    @Override
    public List<TagResponseDto> searchTag(String inputName) {
        String splitTagName = splitTagName(inputName);
        return tagRepository.searchTagForAutoSearchName(splitTagName);
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
