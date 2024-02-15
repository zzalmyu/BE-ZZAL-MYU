package com.prgrms.zzalmyu.domain.report.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.prgrms.zzalmyu.domain.report.domain.entity.QReport.report;

@RequiredArgsConstructor
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public LocalDateTime getLastReportAt(Long imageId) {
        return queryFactory
                .select(report.createdAt)
                .from(report)
                .orderBy(report.createdAt.desc())
                .fetchFirst();
    }
}
