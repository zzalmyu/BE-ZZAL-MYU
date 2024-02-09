package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTagRepository extends JpaRepository<ImageTag, Long>, ImageTagRepositoryCustom {
}
