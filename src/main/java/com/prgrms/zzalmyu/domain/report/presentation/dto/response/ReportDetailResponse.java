package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDetailResponse {

    private LocalDateTime reportDate;

    private String reportUserEmail;

    private List<TagResponseDto> tags;

    public static ReportDetailResponse of(Report report, User user, List<TagResponseDto> tags) {
        return new ReportDetailResponse(report.getCreatedAt(), user.getEmail(), tags);
    }
}
