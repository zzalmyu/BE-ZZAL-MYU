package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;

import java.util.List;

public interface ImageTagRepositoryCustom {
    List<Image> findLikeImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList);
    List<Image> findUploadImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList);
    List<ImageResponseDto> findImageByTagIdAndLimit(Long tagId, int limit);
    List<Image> findImagesByTagIdList(List<Long> tagIdList);
}
