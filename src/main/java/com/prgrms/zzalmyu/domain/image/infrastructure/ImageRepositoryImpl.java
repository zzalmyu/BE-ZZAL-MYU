package com.prgrms.zzalmyu.domain.image.infrastructure;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.image.presentation.dto.res.ImageResponseDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.prgrms.zzalmyu.domain.image.domain.entity.QImage.image;
import static com.prgrms.zzalmyu.domain.image.domain.entity.QImageLike.imageLike;

@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //사용자가 많이 사용한 순으로 이미지 뽑아내기
    @Override
    public List<ImageResponseDto> getTopUserUsedImage(Pageable pageable) {

        // 좋아요 많은 순으로 하기. 태그 기반은 페이징 처리시 중복 못걸러냄
        return queryFactory
                .select(Projections.constructor(ImageResponseDto.class,image.id,image.title,image.path))
                .from(image)
                .join(imageLike).on(image.id.eq(imageLike.image.id))
                .groupBy(imageLike.image.id)
                .orderBy(imageLike.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

    @Override
    public List<Image> findTopImageLike(int limit) {
        return queryFactory
                .select(image)
                .from(imageLike)
                .join(image).on(image.id.eq(imageLike.image.id))
                .groupBy(imageLike.image.id)
                .orderBy(imageLike.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Image> findImageLikesByUserId(Long userId, Pageable pageable) {
        return queryFactory.selectFrom(image)
                .join(imageLike).on(image.id.eq(imageLike.image.id))
                .where(imageLike.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<ImageResponseDto> findByUserId(Long userId, Pageable pageable) {

        return queryFactory.select(Projections.constructor(ImageResponseDto.class,
                        image.id,image.title,image.path,imageLike.id.isNotNull()))
                .from(image)
                .leftJoin(imageLike).on(image.id.eq(imageLike.image.id))
                .where(image.userId.eq(userId))
                .fetch();
    }
}









