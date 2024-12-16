package com.project.hit.report;

import com.project.hit.subject.Subject;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public void insertReport(Report report) {
        reportRepository.save(report);
    }

    public List<Report> getReportsListBySubject(Subject subject) {
        return this.reportRepository.findReportBySubject(subject);
    }

    public Page<Report> getReportsPagingList(Subject subject, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("no"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return this.reportRepository.findReportBySubject(subject, pageable);
    }

    public Report getReportByNo(int no) {
        return this.reportRepository.findReportByNo(no);
    }
}
