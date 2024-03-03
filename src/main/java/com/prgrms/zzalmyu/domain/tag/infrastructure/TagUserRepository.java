package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagUserRepository extends JpaRepository<TagUser, Long> {

    Optional<TagUser> findByTagIdAndUserId(Long tagId,Long userId);

    List<TagUser> findByUserIdOrderByCountDesc(Long userId);

}
