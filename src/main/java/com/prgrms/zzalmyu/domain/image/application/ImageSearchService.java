package com.prgrms.zzalmyu.domain.image.application;

import java.util.List;

public interface ImageSearchService {

    /**
     * 좋아요한 짤 태그 필터링
     * @param tagIdList
     */
    void searchLikeImages(List<Long> tagIdList);

    /**
     * 업로드한 짤 태그 필터링
     * @param tagIdList
     */
    void searchUploadImages(List<Long> tagIdList);
}
