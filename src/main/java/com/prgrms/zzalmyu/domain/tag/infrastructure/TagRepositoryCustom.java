package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;

import java.util.List;

public interface TagRepositoryCustom {

    List<TagResponseDto> getTopTagsFromUserUsed(int limit);
    List<TagResponseDto> getTopTagsFromLikedImages(Long userId, int limit);
    List<TagResponseDto> getTopTagsFromUploadImages(Long userId, int limit);
    List<TagResponseDto> searchTagForAutoSearchName(String inputString);
    List<TagResponseDto> searchTagForAutoSearchNameFromLikeImages(Long userId, String inputString);
}
