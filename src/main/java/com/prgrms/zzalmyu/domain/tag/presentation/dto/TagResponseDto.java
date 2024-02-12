package com.prgrms.zzalmyu.domain.tag.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagResponseDto {
    private Long tagId;
    private String tagName;
    private Integer tagUserCount;

    @Builder
    public TagResponseDto(Long tagId, String tagName, Integer tagUserCount) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagUserCount = tagUserCount;
    }

}


