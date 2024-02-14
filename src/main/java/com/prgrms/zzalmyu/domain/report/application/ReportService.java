package com.prgrms.zzalmyu.domain.report.application;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.report.infrastructure.ReportRepository;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportDetailResponse;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportResponse;
import com.prgrms.zzalmyu.domain.user.application.UserService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserService userService;

    public void reportImage(Long userId, Long imageId) {
        Report report = Report.builder()
            .reportUserId(userId)
            .imageId(imageId)
            .build();
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<ReportDetailResponse> getReportDetail(Long imageId) {
        List<Report> reports = reportRepository.findByImageId(imageId);
        List<ReportDetailResponse> responses = reports.stream()
            .map(report -> {
                User reportUser = userService.findUserById(report.getId());
                return ReportDetailResponse.of(report, reportUser);
            })
            .collect(Collectors.toList());

        return responses;
    }

    public List<ReportResponse> getReports() {
        
    }
}
