package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReportDetailResponse {
    private String imageUrl;
    private String imageTitle;
    private List<TagResponseDto> tags;
    private List<ReportDetailDto> reports;

    public static ReportDetailResponse of(List<TagResponseDto> tags, Image image, List<ReportDetailDto> reports) {
        return new ReportDetailResponse(image.getPath(), image.getTitle(), tags, reports);
    }
}
