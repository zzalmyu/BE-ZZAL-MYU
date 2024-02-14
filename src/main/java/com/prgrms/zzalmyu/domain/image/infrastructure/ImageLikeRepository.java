package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.ImageLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageLikeRepository extends JpaRepository<ImageLike, Long> {
    Optional<ImageLike> findByUserIdAndImageId(Long userId, Long imageId);
    void deleteImageLikeByImageId(Long imageId);
}
