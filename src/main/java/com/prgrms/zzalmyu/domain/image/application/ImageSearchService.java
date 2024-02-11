package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.util.List;

public interface ImageSearchService {

    /**
     * 좋아요한 짤 태그 필터링
     *
     * @param tagIdList
     */
    List<AwsS3ResponseDto> searchLikeImages(User user, List<Long> tagIdList);

    /**
     * 업로드한 짤 태그 필터링
     *
     * @param tagIdList
     */
    List<AwsS3ResponseDto> searchUploadImages(User user, List<Long> tagIdList);
}
