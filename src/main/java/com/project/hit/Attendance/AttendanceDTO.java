package com.project.hit.Attendance;

import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import com.project.hit.sugang.Sugang;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDTO {
    private Long id;
    private String attendanceStatus;
    private String studentName;
    private String date;
    private String dayOfWeek;
    private String subjectName;
    private String studentNo;
}
