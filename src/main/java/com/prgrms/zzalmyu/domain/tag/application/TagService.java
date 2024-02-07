package com.prgrms.zzalmyu.domain.tag.application;

public interface TagService {

    /**
     * 짤 업로드 페이지에서의 인기(추천) 태그 조회
     */
    void getTopTags();

    /**
     * 좋아요한 사진 들 중 공통된 상위 5개 태그 조회
     */
    void getTopTagsFromLikeImages();

    /**
     * 업로드한 사진 들 중 공통된 상위 5개 태그 조회
     */
    void getTopTagsFromUploadImages();


    /**
     * 태그 생성
     */
    void createTag(String tagName);
}
