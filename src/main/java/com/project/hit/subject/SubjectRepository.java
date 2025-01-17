package com.project.hit.subject;

import com.project.hit.major.Major;
import com.project.hit.professor.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Subject findByNo(int no);

    List<Subject> findAll();

    List<Subject> findSubjectByTypeContainingOrderByNoDesc(String type);

    // 전공 별 과목 페이징
    Page<Subject> findSubjectByMajorAndSemesterContainingAndYearContaining(Major major, String semester, String year, Pageable pageable);

    // 교양 과목 페이징
    Page<Subject> findSubjectByTypeContainingAndSemesterContainingAndYearContaining(String type, String semester, String year, Pageable pageable);

    // 전체 과목 페이징
    Page<Subject> findAllBySemesterContainingAndYearContaining(String semester, String year, Pageable pageable);

    // 과목기입 페이징
    Page<Subject> findAll(Pageable pageable);

    // 타입 별 과목 페이징
    Page<Subject> findSubjectByTypeContaining(String type, Pageable pageable);

    Page<Subject> findSubjectByMajor(Major major, Pageable pageable);

    @Query("select distinct s from Subject s where s.professor = :professor and s.year = :year and s.semester = :semester order by s.no desc")
    List<Subject> findSubjectByProfessor(Professor professor, String year, String semester);

    @Query("select distinct s from Subject s where s.professor = :professor and s.year = :year and s.semester = :semester order by s.no desc")
    Page<Subject> findSubjectPageByProfessor(Professor professor, String year, String semester, Pageable pageable);

    @Query("select distinct s from Subject s where s.professor = :professor and s.year = :year and s.semester = :semester order by s.no desc limit 1")
    Subject findTopByProfessorOrderByNoDesc(Professor professor, String year, String semester);
}


