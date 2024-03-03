package com.prgrms.zzalmyu.domain.image.presentation.dto.res;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponseDto {
    private Long imageId;
    private String title;
    private String path;
    private boolean imageLikeYn;
    @Builder
    public ImageResponseDto(Long imageId, String title, String path,boolean imageLikeYn) {
        this.imageId = imageId;
        this.title = title;
        this.path = path;
        this.imageLikeYn = imageLikeYn;
    }
    public ImageResponseDto(Long imageId, String title, String path) {
        this.imageId = imageId;
        this.title = title;
        this.path = path;
        this.imageLikeYn = false;
    }

}
