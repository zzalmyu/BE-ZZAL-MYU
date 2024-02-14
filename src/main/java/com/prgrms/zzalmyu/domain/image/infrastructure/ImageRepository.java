package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i join ImageLike k on i.id = k.image.id where k.user.id= :userId")
    List<Image> findImageLikesByUserId(Long userId);
    @Query("select t from Tag t join ImageTag i on t.id = i.tag.id where  i.image.id = :imageId")
    List<Tag> findTagsByImageId(Long imageId);

    List<Image>findByUserId(Long userId);
}
