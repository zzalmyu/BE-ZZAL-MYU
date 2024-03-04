package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageRepositoryCustom {
    List<ImageResponseDto> getTopUserUsedImage(Pageable pageable);
}
