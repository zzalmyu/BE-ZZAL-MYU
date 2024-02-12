package com.prgrms.zzalmyu.domain.tag.infrastructure;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.TagResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageLike.imageLike;
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
                        tag.name,
                        tagUser.count.sum()))
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
                        tag.name,
                        tagUser.count.sum()))
                .from(tag)
                .join(tagUser).on(tagUser.tagId.eq(tag.id))
                .join(imageLike).on(imageLike.user.id.eq(tagUser.userId))
                .where(tagUser.userId.eq(userId))
                .groupBy(tag.id, tag.name)
                .orderBy(tagUser.count.sum().desc())
                .limit(limit)
                .fetch();
    }
}
