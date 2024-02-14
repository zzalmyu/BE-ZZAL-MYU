package com.prgrms.zzalmyu.domain.report.infrastructure;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByImageId(Long imageId);

    @Query("select count(r) from Report r where r.imageId = :imageId")
    Long countByImageId(Long imageId);
}
