package com.prgrms.zzalmyu.domain.image.domain.entity;

import com.prgrms.zzalmyu.domain.user.domain.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "image_id", nullable = false)
    private Image imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id", nullable = false)
    private User userId;

    @Builder
    private ImageLike(Image imageId, User userId) {
        this.imageId = imageId;
        this.userId = userId;
    }
}
