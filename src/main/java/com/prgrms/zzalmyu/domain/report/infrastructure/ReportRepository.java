package com.prgrms.zzalmyu.domain.report.infrastructure;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

    List<Report> findByImageId(Long imageId);

    @Query("select count(r) from Report r where r.imageId = :imageId")
    int countByImageId(Long imageId);

    LocalDateTime getLastReportAt(Long imageId);

    @Query("SELECT r.imageId FROM Report r GROUP BY r.imageId HAVING COUNT(*) >= 3")
    List<Long> getImageIdReportedOverThree(Pageable pageable);

    @Query("SELECT r.imageId FROM Report r GROUP BY r.imageId HAVING COUNT(*) >= 3 and min(r.createdAt) < :fiveDaysAgo")
    List<Long> getImageIdReportedOverThreeFiveDaysAgo(LocalDateTime fiveDaysAgo);

    void deleteByImageId(Long imageId);

    Optional<Report> findByImageIdAndReportUserId(Long imageId, Long userId);
}
