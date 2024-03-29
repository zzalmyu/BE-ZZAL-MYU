package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagMeResponseDto;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
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
    private static final Long LIMIT_TAG_NUM = 10L;

    @Override
    public List<TagResponseDto> getTopTagsFromUserUsed(int limit) {
        return queryFactory
                .select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name,
                        tagUser.count.sum().as("count")))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<TagMeResponseDto> getTopTagsFromLikedImages(Long userId, int limit) {
        return queryFactory.select(Projections.constructor(
                        TagMeResponseDto.class,
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
    public List<TagMeResponseDto> getTopTagsFromUploadImages(Long userId, int limit) {
        return queryFactory.select(Projections.constructor(
                        TagMeResponseDto.class,
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
                        tag.name,
                        tagUser.count.sum().as("count")))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .where(tag.splitName.startsWith(inputString))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(LIMIT_TAG_NUM)
                .fetch();
    }

    @Override
    public List<TagResponseDto> searchTagForAutoSearchNameFromLikeImages(Long userId, String inputString) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name,
                        tagUser.count.sum().as("count")))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .join(imageTag).on(imageTag.tag.eq(tag))
                .join(imageLike).on(imageLike.image.eq(imageTag.image))
                .where(imageLike.user.id.eq(userId),
                        tag.splitName.startsWith(inputString))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(LIMIT_TAG_NUM)
                .distinct()
                .fetch();
    }

    @Override
    public List<TagResponseDto> searchTagForAutoSearchNameFromUploadImages(Long userId, String inputString) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class,
                        tag.id,
                        tag.name,
                        tagUser.count.sum().as("count")))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .join(imageTag).on(imageTag.tag.eq(tag))
                .join(image).on(image.eq(imageTag.image))
                .where(image.userId.eq(userId),
                        tag.splitName.startsWith(inputString))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(LIMIT_TAG_NUM)
                .distinct()
                .fetch();
    }

    @Override
    public List<TagResponseDto> getRecommendationTags(User user) {
        return queryFactory.select(Projections.constructor(
                        TagResponseDto.class, tag.id, tag.name))
                .from(tag)
                .join(tagUser).on(tag.id.eq(tagUser.tagId))
                .where(tagUser.userId.eq(user.getId()))
                .groupBy(tagUser.tagId)
                .orderBy(tagUser.count.sum().desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<Long> findTagIdListByTagNameList(List<String> tagNameList) {
        return queryFactory.select(tag.id)
                .from(tag)
                .where(tag.name.in(tagNameList))
                .fetch();
    }
}
