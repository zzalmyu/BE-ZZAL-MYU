package com.prgrms.zzalmyu.domain.tag.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Table(name = "tag_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "split_name")
    private String splitName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Tag(String name) {
        this.name = name;
    }
    private Tag(String name, String splitName) {
        this.name = name;
        this.splitName = splitName;
    }

    public static Tag from(String name) {
        return new Tag(name);
    }

    public static Tag from(String tagName, String splitName) {
        return new Tag(tagName, splitName);
    }
}
