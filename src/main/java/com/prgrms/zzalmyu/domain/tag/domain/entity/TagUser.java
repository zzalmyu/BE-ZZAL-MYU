package com.prgrms.zzalmyu.domain.tag.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tag_user_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자가 태그 사용한 횟수
    @Column(name = "count_quantity")
    private Integer count;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Builder
    private TagUser(Long userId, Long tagId) {
        this.count = 0;
        this.userId = userId;
        this.tagId = tagId;
    }

    public void increaseCount() {
        this.count++;
    }
}
