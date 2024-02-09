package com.prgrms.zzalmyu.domain.chat.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
