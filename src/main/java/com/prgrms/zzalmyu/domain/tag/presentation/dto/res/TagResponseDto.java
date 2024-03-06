package com.prgrms.zzalmyu.domain.tag.presentation.dto.res;

import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.tag.domain.entity.TagUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagResponseDto {
    private Long tagId;
    private String tagName;
    private Integer count;

    @Builder
    public TagResponseDto(Long tagId, String tagName, Integer count) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.count = count;
    }

    public TagResponseDto(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public TagResponseDto(Tag tag) {
        this(tag.getId(), tag.getName());
    }

    public static TagResponseDto from(Tag tag, TagUser tagUser) {
        return TagResponseDto.builder()
                .tagId(tag.getId())
                .tagName(tag.getName())
                .count(tagUser.getCount())
                .build();
    }

}


