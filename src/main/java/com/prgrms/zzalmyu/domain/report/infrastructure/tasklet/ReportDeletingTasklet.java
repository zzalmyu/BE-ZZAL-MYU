package com.prgrms.zzalmyu.domain.report.infrastructure.tasklet;

import com.prgrms.zzalmyu.domain.image.application.ImageRemoveService;
import com.prgrms.zzalmyu.domain.report.infrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ReportDeletingTasklet implements Tasklet, InitializingBean {

    private final ReportRepository reportRepository;
    private final ImageRemoveService imageRemoveService;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDateTime fiveDaysAgo = LocalDateTime.of(today.minusDays(5), LocalTime.MIDNIGHT);
        List<Long> imageIdReportedOverThreeAgo = reportRepository.getImageIdReportedOverThreeFiveDaysAgo(fiveDaysAgo);

        try {
            imageIdReportedOverThreeAgo
                    .forEach(imageId -> {
                        reportRepository.deleteByImageId(imageId);
                        imageRemoveService.deleteReportImage(imageId);
                    });
        } catch (Exception e) {
            log.error("신고된 이미지 자동 삭제 예외 발생");
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
