package com.prgrms.zzalmyu.domain.image.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AwsS3RequestDto {

    private Long imageId;
    private String path;

    @Builder
    public AwsS3RequestDto(Long imageId, String path) {
        this.imageId = imageId;
        this.path = path;
    }
}
