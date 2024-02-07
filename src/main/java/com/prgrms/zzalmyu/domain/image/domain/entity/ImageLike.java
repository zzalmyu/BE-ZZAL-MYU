package com.prgrms.zzalmyu.domain.image.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "image_like_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Builder
    private ImageLike(Long imageId, Long userId) {
        this.imageId = imageId;
        this.userId = userId;
    }
}
