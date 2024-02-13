package com.prgrms.zzalmyu.domain.image.presentation.dto.res;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AwsS3ResponseDto {

    private Long imageId;
    private String path;

    @Builder
    private AwsS3ResponseDto(Long imageId, String path) {
        this.imageId = imageId;
        this.path = path;
    }

    public AwsS3ResponseDto(Image image) {
        this(image.getId(), image.getPath());
    }
}
