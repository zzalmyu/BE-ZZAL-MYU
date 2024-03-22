package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponse {
    private Long imageId;
    private String imageTitle;
    private LocalDateTime lastReportAt;
    private int reportCount;


    public static ReportResponse of(Long imageId, String imageTitle, LocalDateTime thirdReportDate, int reportCount) {
        return ReportResponse.builder()
                .imageId(imageId)
                .imageTitle(imageTitle)
                .lastReportAt(thirdReportDate)
                .reportCount(reportCount)
                .build();
    }
}
