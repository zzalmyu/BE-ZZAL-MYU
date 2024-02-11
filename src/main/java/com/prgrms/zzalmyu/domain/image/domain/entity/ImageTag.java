package com.prgrms.zzalmyu.domain.image.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Builder
    private ImageTag(Long imageId, Long tagId) {
        this.imageId = imageId;
        this.tagId = tagId;
    }
}
