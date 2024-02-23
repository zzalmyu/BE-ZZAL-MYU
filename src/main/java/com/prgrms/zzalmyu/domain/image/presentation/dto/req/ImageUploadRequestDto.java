package com.prgrms.zzalmyu.domain.image.presentation.dto.req;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUploadRequestDto {
    private String title;
    private List<Long> tagIdList;
}
