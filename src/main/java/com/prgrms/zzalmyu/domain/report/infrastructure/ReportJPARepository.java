package com.prgrms.zzalmyu.domain.report.infrastructure;

import com.prgrms.zzalmyu.domain.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJPARepository extends JpaRepository<Report, Long> {
}
