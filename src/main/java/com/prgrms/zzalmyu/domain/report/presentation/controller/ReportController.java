package com.prgrms.zzalmyu.domain.report.presentation.controller;

import com.prgrms.zzalmyu.domain.report.application.ReportService;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportDetailResponse;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportResponse;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    @ApiResponse(description = "짤 신고하기")
    @PostMapping("/{imageId}")
    public ResponseEntity<Void> reportImage(@AuthenticationPrincipal User user, @PathVariable Long imageId) {
        reportService.reportImage(user.getId(), imageId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(description = "신고된 짤 내역 상세보기")
    @GetMapping("/{imageId}")
    public ResponseEntity<List<ReportDetailResponse>> getReportedImage(@PathVariable Long imageId) {
        List<ReportDetailResponse> responses = reportService.getReportDetail(imageId);
        return ResponseEntity.ok(responses);
    }

    @ApiResponse(description = "신고된 짤 삭제(처리)하기")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteReportedImage(@PathVariable Long imageId) {
        reportService.deleteReportedImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(description = "신고된 짤 리스트 반환")
    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports(@PageableDefault(page = 0,size = 10) Pageable pageable) {
        List<ReportResponse> responses = reportService.getReports(pageable);
        return ResponseEntity.ok(responses);
    }
}
