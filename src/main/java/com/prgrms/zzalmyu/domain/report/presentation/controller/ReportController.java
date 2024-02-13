package com.prgrms.zzalmyu.domain.report.presentation.controller;

import com.prgrms.zzalmyu.domain.report.application.ReportService;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportDetailResponse;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{imageId}")
    public ResponseEntity<Void> reportImage(@AuthenticationPrincipal User user, @PathVariable Long imageId) {
        reportService.reportImage(user.getId(), imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<List<ReportDetailResponse>> getReportedImage(@PathVariable Long imageId) {
        List<ReportDetailResponse> responses = reportService.getReportDetail(imageId);
        return ResponseEntity.ok(responses);
    }
}
