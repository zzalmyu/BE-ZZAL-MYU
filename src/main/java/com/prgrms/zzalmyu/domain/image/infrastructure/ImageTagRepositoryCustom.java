package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageTagRepositoryCustom {
    List<Image> findLikeImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList, Pageable pageable);
    List<ImageResponseDto> findUploadImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList, Pageable pageable);
    List<ImageResponseDto> findImageByTagIdAndLimit(Long tagId, Long userId, int limit);
    List<Image> findImagesByTagIdList(List<Long> tagIdList, Pageable pageable);
    List<ImageResponseDto> findImagesByTagIdListAndUser(List<Long> tagIdList,Long userId, Pageable pageable);
}
