package com.prgrms.zzalmyu.domain.chat.infrastructure;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageChatCountRepository extends JpaRepository<ImageChatCount, Long> {

}
