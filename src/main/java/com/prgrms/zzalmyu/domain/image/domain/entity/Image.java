package com.prgrms.zzalmyu.domain.image.domain.entity;

import com.prgrms.zzalmyu.domain.chat.domain.entity.ImageChatCount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Table(name = "image_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @Column(name = "url", nullable = false)
    private String path;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_count_id", nullable = false)
    private ImageChatCount imageChatCount;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    private Image(String title, String s3Key, String path, ImageChatCount imageChatCount, Long userId) {
        this.title = title;
        this.s3Key = s3Key;
        this.path = path;
        this.imageChatCount = imageChatCount;
        this.userId = userId;
    }
}
