package com.prgrms.zzalmyu.domain.report.infrastructure;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

    List<Report> findByImageId(Long imageId);

    @Query("select count(r) from Report r where r.imageId = :imageId")
    int countByImageId(Long imageId);

    LocalDateTime getThirdReportAt(Long imageId);

    @Query("SELECT r.imageId FROM Report r GROUP BY r.imageId HAVING COUNT(*) >= 3")
    List<Long> getImageIdReportedOverThree();
}
