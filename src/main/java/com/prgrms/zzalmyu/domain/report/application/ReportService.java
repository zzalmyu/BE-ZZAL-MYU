package com.prgrms.zzalmyu.domain.report.application;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.report.infrastructure.ReportJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportJPARepository reportJPARepository;

    public void reportImage(Long userId, Long imageId) {
        Report report = Report.builder()
            .reportUserId(userId)
            .imageId(imageId)
            .build();
        reportJPARepository.save(report);
    }
}
