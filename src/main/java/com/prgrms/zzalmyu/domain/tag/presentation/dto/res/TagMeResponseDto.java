package com.prgrms.zzalmyu.domain.tag.presentation.dto.res;

import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagMeResponseDto {
    private Long tagId;
    private String tagName;

    public TagMeResponseDto(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public TagMeResponseDto(Tag tag) {
        this(tag.getId(), tag.getName());
    }
}
