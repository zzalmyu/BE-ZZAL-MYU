package com.prgrms.zzalmyu.domain.image.domain.entity;

import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "image_tag_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    private ImageTag(Image image, Tag tag) {
        this.image = image;
        this.tag = tag;
    }
}
