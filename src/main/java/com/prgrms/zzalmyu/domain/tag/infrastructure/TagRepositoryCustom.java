package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagMeResponseDto;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.util.List;

public interface TagRepositoryCustom {

    List<TagResponseDto> getTopTagsFromUserUsed(int limit);
    List<TagMeResponseDto> getTopTagsFromLikedImages(Long userId, int limit);
    List<TagMeResponseDto> getTopTagsFromUploadImages(Long userId, int limit);
    List<TagResponseDto> searchTagForAutoSearchName(String inputString);
    List<TagResponseDto> searchTagForAutoSearchNameFromLikeImages(Long userId, String inputString);
    List<TagResponseDto> searchTagForAutoSearchNameFromUploadImages(Long userId, String inputString);
    List<TagResponseDto> getRecommendationTags(User user);
    List<Long> findTagIdListByTagNameList(List<String> tagNameList);
}
