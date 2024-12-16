package com.project.hit.report;

import com.project.hit.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findReportBySubject(Subject subject);

    Page<Report> findReportBySubject(Subject subject, Pageable pageable);

    Report findReportByNo(int no);
}
