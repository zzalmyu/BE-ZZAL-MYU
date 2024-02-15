package com.prgrms.zzalmyu.domain.tag.presentation.dto.req;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagCreateRequestDto {

    String name;

    public TagCreateRequestDto(String name) {
        this.name = name;
    }
}
