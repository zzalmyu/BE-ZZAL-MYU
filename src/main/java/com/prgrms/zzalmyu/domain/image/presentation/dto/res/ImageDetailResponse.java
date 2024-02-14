package com.prgrms.zzalmyu.domain.image.presentation.dto.res;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDetailResponse {
    private Long imageId;
    private Long userId;
    private String imgUrl;
    private boolean imageLikeYn; // 상세페이지 반환할 때, 사용자의 좋아요 유무
    private List<Tag> tags; //TagResponse 로 수정 필요

    @Builder
    private ImageDetailResponse(Long imageId,Long userId, String imgUrl, boolean imageLikeYn, List<Tag> tags) {
        this.imageId = imageId;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.imageLikeYn = imageLikeYn;
        this.tags = tags;
    }

    public static ImageDetailResponse of(Image image, List<Tag>tags, boolean image_like_yn) {
        return ImageDetailResponse.builder()
                .imageId(image.getId())
                .imgUrl(image.getPath())
                .imageLikeYn(image_like_yn)
                .tags(tags).build();
    }
}
