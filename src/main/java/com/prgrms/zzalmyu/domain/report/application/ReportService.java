package com.prgrms.zzalmyu.domain.report.application;

import com.prgrms.zzalmyu.core.properties.ErrorCode;
import com.prgrms.zzalmyu.domain.image.application.ImageRemoveService;
import com.prgrms.zzalmyu.domain.image.infrastructure.ImageRepository;
import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.report.exception.ReportException;
import com.prgrms.zzalmyu.domain.report.infrastructure.ReportRepository;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportDetailResponse;
import com.prgrms.zzalmyu.domain.tag.domain.entity.Tag;
import com.prgrms.zzalmyu.domain.tag.presentation.dto.res.TagResponseDto;
import com.prgrms.zzalmyu.domain.report.presentation.dto.response.ReportResponse;
import com.prgrms.zzalmyu.domain.user.application.UserService;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.time.LocalDateTime;
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
    private final ImageRemoveService imageRemoveService;
    private final ImageRepository imageRepository;

    public void reportImage(Long userId, Long imageId) {
        if(!reportRepository.findByImageIdAndReportUserId(imageId, userId).isEmpty()) {
            throw new ReportException(ErrorCode.REPORT_ALREADY_EXIST_ERROR);
        }
        Report report = Report.builder()
                .reportUserId(userId)
                .imageId(imageId)
                .build();
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<ReportDetailResponse> getReportDetail(Long imageId) {
        List<Report> reports = reportRepository.findByImageId(imageId);
        if (reports.isEmpty()) {
            throw new ReportException(ErrorCode.REPORT_NOT_FOUND);
        }

        List<ReportDetailResponse> responses = reports.stream()
                .map(report -> {
                    User reportUser = userService.findUserById(report.getReportUserId());
                    List<TagResponseDto> tags = getTags(report.getImageId());
                    return ReportDetailResponse.of(report, reportUser, tags);
                })
                .collect(Collectors.toList());

        return responses;
    }

    public void deleteReportedImage(Long imageId) {
        int reportCount = reportRepository.countByImageId(imageId);
        if (reportCount >= 3) {
            imageRemoveService.deleteReportImage(imageId);
        } else {
            throw new ReportException(ErrorCode.IMAGE_DELETION_NOT_ALLOWED);
        }
    }

    private List<TagResponseDto> getTags(Long imageId) {
        List<Tag> tags = imageRepository.findTagsByImageId(imageId);
        return tags.stream()
                .map(tag -> new TagResponseDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getReports() {
        List<Long> reportedImageIds = reportRepository.getImageIdReportedOverThree();
        List<ReportResponse> responses = reportedImageIds.stream()
                .map(imageId -> {
                    LocalDateTime lastReportDate = reportRepository.getLastReportAt(imageId);
                    int reportCount = reportRepository.countByImageId(imageId);
                    List<TagResponseDto> tags = getTags(imageId);
                    return ReportResponse.of(lastReportDate, reportCount, tags);
                })
                .collect(Collectors.toList());

        return responses;
    }
}
