package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagUserRepository extends JpaRepository<TagUser, Long> {
}
