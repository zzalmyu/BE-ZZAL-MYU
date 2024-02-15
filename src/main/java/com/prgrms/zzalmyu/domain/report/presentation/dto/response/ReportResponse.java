package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReportResponse {

    private LocalDateTime lastReportAt;

    private int reportCount;

    private List<TagResponseDto> tags;

    public static ReportResponse of(LocalDateTime thirdReportDate, int reportCount, List<TagResponseDto> tags) {
        return ReportResponse.builder()
                .lastReportAt(thirdReportDate)
                .reportCount(reportCount)
                .tags(tags)
                .build();
    }
}
