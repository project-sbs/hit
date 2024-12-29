package com.project.hit.Attendance;


import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query ("select distinct att from Attendance att where att.date = :date")
    List<Attendance> findAtytendancefromDate(String date);
    List<Attendance> findAttendanceByDate(String date);

    List<AttendanceDTO> findByStudentIdAndDate(String studentId, String date);

    Attendance findByStudentAndSubjectAndDate(Student student, Subject subject, String date);


}
