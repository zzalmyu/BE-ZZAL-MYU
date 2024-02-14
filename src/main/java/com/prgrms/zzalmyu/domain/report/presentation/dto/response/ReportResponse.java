package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponse {

    private Long imageId;

    private LocalDateTime reportAt;

    private int reportCount;
}
