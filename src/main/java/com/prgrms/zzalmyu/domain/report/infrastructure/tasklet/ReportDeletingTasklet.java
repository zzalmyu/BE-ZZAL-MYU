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
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ReportDeletingTasklet implements Tasklet, InitializingBean {

    private final ReportRepository reportRepository;
    private final ImageRemoveService imageRemoveService;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Long> imageIdReportedOverThree = reportRepository.getImageIdReportedOverThree();
        try {
            imageIdReportedOverThree
                    .forEach(imageRemoveService::deleteReportImage);
        } catch (Exception e) {
            log.error("신고된 이미지 자동 삭제 예외 발생");
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
