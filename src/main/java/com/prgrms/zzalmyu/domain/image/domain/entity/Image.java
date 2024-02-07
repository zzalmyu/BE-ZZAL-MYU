package com.prgrms.zzalmyu.domain.image.domain.entity;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "image_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_count_id", nullable = false)
    private ImageChatCount imageChatCount;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    private Image(String url, ImageChatCount imageChatCount, Long userId) {
        this.url = url;
        this.imageChatCount = imageChatCount;
        this.userId = userId;
    }
}
