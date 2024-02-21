package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.prgrms.zzalmyu.domain.image.domain.entity.QImage.image;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageLike.imageLike;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageTag.imageTag;
import static com.prgrms.zzalmyu.domain.tag.domain.entity.QTag.tag;
import static com.prgrms.zzalmyu.domain.tag.domain.entity.QTagUser.tagUser;

@RequiredArgsConstructor
public class TagRepositoryCustomImpl implements TagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TagResponseDto> getTopTagsFromUserUsed(int limit) {
        return queryFactory
                .select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<TagResponseDto> getTopTagsFromLikedImages(Long userId, int limit) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .join(imageLike).on(imageLike.user.id.eq(tagUser.userId))
                .where(tagUser.userId.eq(userId))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<TagResponseDto> getTopTagsFromUploadImages(Long userId, int limit) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .join(image).on(image.userId.eq(tagUser.userId))
                .where(image.userId.eq(userId))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<TagResponseDto> searchTagForAutoSearchName(String inputString) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name))
                .from(tag)
                .where(tag.splitName.startsWith(inputString))
                .fetch();
    }

    @Override
    public List<TagResponseDto> searchTagForAutoSearchNameFromLikeImages(Long userId, String inputString) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name))
                .from(tag)
                .join(imageTag).on(imageTag.tag.eq(tag))
                .join(imageLike).on(imageLike.image.eq(imageTag.image))
                .where(imageLike.user.id.eq(userId),
                        tag.splitName.startsWith(inputString))
                .distinct()
                .fetch();
    }
}
