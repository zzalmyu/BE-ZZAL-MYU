package com.prgrms.zzalmyu.domain.image.application;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageSearchService {

    /**
     * 좋아요한 짤 태그 필터링
     *
     */
    List<ImageResponseDto> searchLikeImages(User user, List<String> tagNames, Pageable pageable);

    /**
     * 업로드한 짤 태그 필터링
     */
    List<ImageResponseDto> searchUploadImages(User user, List<String> tagNames, Pageable pageable);

    /**
     * 전체 짤 태그 필터링(로그인X유저)
     */
    List<ImageResponseDto> searchImages(List<String> tagNames, Pageable pageable);
    /**
     * 전체 짤 태그 필터링(로그인 유저)
     */
    List<ImageResponseDto> searchImagesByUser(List<String>tagNames, User user, Pageable pageable);

}
