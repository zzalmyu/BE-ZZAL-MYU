package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageTagRepository extends JpaRepository<ImageTag, Long>, ImageTagRepositoryCustom {

    @Query("select t.id from ImageTag t where t.image.id = :imageId")
    List<Long> findImageTagIdsByImageId(Long imageId);
}
