package com.prgrms.zzalmyu.domain.image.domain.entity;

import com.prgrms.zzalmyu.domain.image.presentation.dto.res.AwsS3ResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AwsS3 {
    private String key;
    private String path;

    @Builder
    public AwsS3(String key, String path) {
        this.key = key;
        this.path = path;
    }

    public AwsS3ResponseDto convertResponseDto(Long imageId, String imageTitle) {
        return AwsS3ResponseDto.builder()
                .imageId(imageId)
                .title(imageTitle)
                .path(this.path)
                .build();
    }
}
