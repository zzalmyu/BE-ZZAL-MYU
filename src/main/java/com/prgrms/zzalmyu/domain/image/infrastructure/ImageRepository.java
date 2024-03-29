package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>,ImageRepositoryCustom {

    @Query("select t from Tag t join ImageTag i on t.id = i.tag.id where  i.image.id = :imageId")
    List<Tag> findTagsByImageId(Long imageId);

}
