package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportDetailDto {
    private LocalDateTime reportDate;
    private String reportUserEmail;
}
