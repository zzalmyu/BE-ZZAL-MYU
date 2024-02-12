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
    public List<TagResponseDto> getTopTags(User user) {
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
    public TagResponseDto createTag(User user, String tagName) {
        if (tagRepository.existsByName(tagName)) {
            throw new TagException(ErrorCode.TAG_ALREADY_EXIST_ERROR);
        }
        Tag newTag = Tag.from(tagName);
        tagRepository.save(newTag);
        return new TagResponseDto(newTag);
    }
}
