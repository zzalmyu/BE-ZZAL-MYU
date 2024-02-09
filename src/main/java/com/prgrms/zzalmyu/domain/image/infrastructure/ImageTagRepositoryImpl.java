package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.prgrms.zzalmyu.domain.image.domain.entity.QImage.image;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageLike.imageLike;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageTag.imageTag;

@RequiredArgsConstructor
public class ImageTagRepositoryImpl implements ImageTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Image> findLikeImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList) {
        return queryFactory.selectFrom(image)
                .join(imageTag).on(imageTag.imageId.eq(image.id))
                .join(imageLike).on(imageLike.userId.eq(userId))
                .where(imageTag.tagId.in(tagIdList))
                .fetch();
    }

    @Override
    public List<Image> findUploadImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList) {
        return queryFactory.selectFrom(image)
                .join(imageTag).on(imageTag.imageId.eq(image.id))
                .where(image.userId.eq(userId),
                        imageTag.tagId.in(tagIdList))
                .fetch();
    }
}
