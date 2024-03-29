package com.prgrms.zzalmyu.domain.report.presentation.dto.response;

import com.prgrms.zzalmyu.domain.image.domain.entity.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReportDetailResponse {
    private String imageUrl;
    private String imageTitle;
    private List<ReportDetailDto> reports;

    public static ReportDetailResponse of(Image image, List<ReportDetailDto> reports) {
        return new ReportDetailResponse(image.getPath(), image.getTitle(), reports);
    }
}
