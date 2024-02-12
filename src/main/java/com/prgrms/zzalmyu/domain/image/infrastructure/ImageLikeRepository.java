package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.ImageLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageLikeRepository extends JpaRepository<ImageLike, Long> {
}
