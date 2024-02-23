package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
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

    private String imageUrl;

    private String imageTitle;

    public static ReportDetailResponse of(Report report, User user, List<TagResponseDto> tags, Image image) {
        return new ReportDetailResponse(report.getCreatedAt(), user.getEmail(), tags, image.getPath(), image.getTitle());
    }
}
