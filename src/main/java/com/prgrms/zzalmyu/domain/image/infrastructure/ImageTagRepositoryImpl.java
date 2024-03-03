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
                .join(imageTag).on(imageTag.image.eq(image))
                .join(imageLike).on(imageLike.image.eq(image))
                .where(imageLike.user.id.eq(userId))
                .where(imageTag.tag.id.in(tagIdList))
                .fetch();
    }

    @Override
    public List<Image> findUploadImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList) {
        return queryFactory.selectFrom(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .where(image.userId.eq(userId),
                        imageTag.tag.id.in(tagIdList))
                .fetch();
    }

    @Override
    public List<Image> findImageByTagIdAndLimit(Long tagId, int limit) {
        return queryFactory.selectFrom(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .where(imageTag.tag.id.eq(tagId))
                .limit(limit)
                .fetch();
    }
}
