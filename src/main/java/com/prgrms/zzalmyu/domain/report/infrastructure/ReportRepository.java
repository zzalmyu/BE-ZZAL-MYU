package com.prgrms.zzalmyu.domain.report.infrastructure;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

    List<Report> findByImageId(Long imageId);

    @Query("select count(r) from Report r where r.imageId = :imageId")
    int countByImageId(Long imageId);

    LocalDateTime getLastReportAt(Long imageId);

    @Query("SELECT r.imageId FROM Report r GROUP BY r.imageId HAVING COUNT(*) >= 3")
    List<Long> getImageIdReportedOverThree(Pageable pageable);

    Optional<Report> findByImageIdAndReportUserId(Long imageId, Long userId);
}
