package com.prgrms.zzalmyu.domain.image.presentation.dto.res;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AwsS3ResponseDto {
    private Long imageId;
    private String title;
    private String path;

    @Builder
    private AwsS3ResponseDto(Long imageId, String title, String path) {
        this.imageId = imageId;
        this.title = title;
        this.path = path;
    }

    public AwsS3ResponseDto(Image image) {
        this(image.getId(), image.getTitle(), image.getPath());
    }
}
