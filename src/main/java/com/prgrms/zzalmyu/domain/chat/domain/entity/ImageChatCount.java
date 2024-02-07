package com.prgrms.zzalmyu.domain.chat.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "image_chat_count_TB")
public class ImageChatCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count_quantity")
    private Integer count;

    public ImageChatCount() {
        this.count = 0;
    }

    public void increaseCount() {
        this.count++;
    }
}
