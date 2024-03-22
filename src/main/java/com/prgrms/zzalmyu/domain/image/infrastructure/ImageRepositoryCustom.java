package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepositoryCustom {
    List<ImageResponseDto> getTopUserUsedImage(Pageable pageable);

    List<ImageResponseDto> findTopImageLike(int limit,Long userId);

    List<Image> findImageLikesByUserId(Long userId, Pageable pageable);
    List<ImageResponseDto> findByUserId(Long userId, Pageable pageable);
    List<ImageResponseDto>findAllByOrderByCreatedAtDesc(User user,int size);

}
