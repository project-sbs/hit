package com.project.hit.grade;

import com.project.hit.student.Student;
import com.project.hit.sugang.Sugang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Grade findGradeBySugang(Sugang sugang);

    @Query("select g from Grade g where g.sugang.subject.no = :subject_no order by g.no desc")
    List<Grade> findGradeBySubject(@Param("subject_no") int subject_no);

    @Query("select distinct g.year, g.semester from Grade g where g.student = :student group by g.year, g.semester order by g.year asc")
    List<String[]> findDistinctYearAndSemesterByStudentNo(@Param("student") Student student);

    List<Grade> findGradeByYearAndSemesterAndStudent(String year, String semester, Student student);

    List<Grade> findGradeByStudent(Student student);
}
