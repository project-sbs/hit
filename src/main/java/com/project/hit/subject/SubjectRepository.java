package com.project.hit.subject;

import com.project.hit.major.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    List<Subject> findAll();

    List<Subject> findSubjectByTypeContainingOrderByNoDesc(String type);

    // 전공 별 과목 페이징
    Page<Subject> findSubjectByMajorAndSemesterContainingAndYearContaining(Major major, String semester, String year, Pageable pageable);

    // 교양 과목 페이징
    Page<Subject> findSubjectByTypeContainingAndSemesterContainingAndYearContaining(String type, String semester, String year, Pageable pageable);

    // 전체 과목 페이징
    Page<Subject> findAllBySemesterContainingAndYearContaining(String semester, String year, Pageable pageable);
}


