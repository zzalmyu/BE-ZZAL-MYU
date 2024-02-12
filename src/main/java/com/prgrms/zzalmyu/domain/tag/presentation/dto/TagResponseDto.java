package com.prgrms.zzalmyu.domain.tag.presentation.dto;

import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagResponseDto {
    private Long tagId;
    private String tagName;

    @Builder
    public TagResponseDto(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public TagResponseDto(Tag tag) {
        this(tag.getId(), tag.getName());
    }

}


