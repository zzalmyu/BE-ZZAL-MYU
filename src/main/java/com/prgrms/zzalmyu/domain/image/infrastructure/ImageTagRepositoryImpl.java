package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.prgrms.zzalmyu.domain.image.domain.entity.QImage.image;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageLike.imageLike;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageTag.imageTag;

@RequiredArgsConstructor
public class ImageTagRepositoryImpl implements ImageTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Image> findLikeImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList, Pageable pageable) {
        return queryFactory.selectFrom(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .join(imageLike).on(imageLike.image.eq(image))
                .where(imageLike.user.id.eq(userId))
                .where(imageTag.tag.id.in(tagIdList))
                .groupBy(image)
                .having(imageTag.tag.id.countDistinct().eq(Long.valueOf(tagIdList.size())))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public List<ImageResponseDto> findUploadImagesByUserIdAndTagIdList(Long userId, List<Long> tagIdList, Pageable pageable) {
        return queryFactory.select(Projections.constructor(ImageResponseDto.class,
                        image.id, image.title, image.path,
                        JPAExpressions
                                .selectOne()
                                .from(imageLike)
                                .where(imageLike.user.id.eq(userId)
                                        .and(imageLike.image.id.eq(image.id)))
                                .exists()))
                .from(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .where(image.userId.eq(userId))
                .where(imageTag.tag.id.in(tagIdList))
                .groupBy(image)
                .having(imageTag.tag.id.countDistinct().eq(Long.valueOf(tagIdList.size())))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public List<ImageResponseDto> findImageByTagIdAndLimit(Long tagId, Long userId, int limit) {
        return queryFactory.select(Projections.constructor(ImageResponseDto.class,
                        image.id, image.title, image.path,
                        JPAExpressions
                                .selectOne()
                                .from(imageLike)
                                .where(imageLike.user.id.eq(userId)
                                        .and(imageLike.image.id.eq(image.id)))
                                .exists()))
                .from(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .where(imageTag.tag.id.eq(tagId))
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Image> findImagesByTagIdList(List<Long> tagIdList, Pageable pageable) {
        return queryFactory.select(image)
                .from(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .where(imageTag.tag.id.in(tagIdList))
                .groupBy(image)
                .having(imageTag.tag.id.countDistinct().eq(Long.valueOf(tagIdList.size())))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public List<ImageResponseDto> findImagesByTagIdListAndUser(List<Long> tagIdList, Long userId, Pageable pageable) {
        return queryFactory.select(Projections.constructor(ImageResponseDto.class,
                        image.id, image.title, image.path,
                        JPAExpressions
                                .selectOne()
                                .from(imageLike)
                                .where(imageLike.user.id.eq(userId)
                                        .and(imageLike.image.id.eq(image.id)))
                                .exists()))
                .from(image)
                .join(imageTag).on(imageTag.image.eq(image))
                .where(imageTag.tag.id.in(tagIdList))
                .groupBy(image)
                .having(imageTag.tag.id.countDistinct().eq(Long.valueOf(tagIdList.size())))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

}
