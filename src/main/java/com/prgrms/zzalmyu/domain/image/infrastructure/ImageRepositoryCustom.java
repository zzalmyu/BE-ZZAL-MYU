package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepositoryCustom {
    List<ImageResponseDto> getTopUserUsedImage(Pageable pageable);

    List<Image> findTopImageLike(int limit);
}
