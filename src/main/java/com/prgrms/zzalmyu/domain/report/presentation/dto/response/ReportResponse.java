package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReportResponse {

    private LocalDateTime reportThirdAt;

    private int reportCount;

    private List<TagResponseDto> tags;
}
