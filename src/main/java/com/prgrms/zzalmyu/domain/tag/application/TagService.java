package com.prgrms.zzalmyu.domain.tag.application;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagMeResponseDto;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.util.List;

public interface TagService {

    /**
     * 짤 업로드 페이지에서의 인기(추천) 태그 조회
     *
     * @return
     */
    List<TagResponseDto> getTopTagsFromUsersUsed();

    /**
     * 좋아요한 사진 들 중 공통된 상위 5개 태그 조회
     *
     * @return
     */
    List<TagMeResponseDto> getTopTagsFromLikeImages(User user);

    /**
     * 업로드한 사진 들 중 공통된 상위 5개 태그 조회
     */
    List<TagMeResponseDto> getTopTagsFromUploadImages(User user);


    /**
     * 태그 생성
     */
    TagResponseDto createTag(User user, String tagName);


    // 전체 태그 자동완성 검색
    List<TagResponseDto> searchTag(String keyword);

    // 좋아요한 사진들의 태그 자동완성 검색
    List<TagResponseDto> searchTagFromLikeImages(User user, String keyword);

    // 업로드한 사진들의 태그 자동완성 검색
    List<TagResponseDto> searchTagFromUploadImages(User user, String keyword);

    // 태그 split_name 분리
    String splitTagName(String tagName);

    List<TagResponseDto> getRecommendationTags(User user);

    // 태그 사용 횟수 증가
    TagResponseDto increaseTagCount(User user, String newTagName);
}
