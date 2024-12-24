package com.project.hit.Attendance;

import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import com.project.hit.sugang.Sugang;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attendanceStatus;
    private String date;
    private String dayOfWeek;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_no", referencedColumnName = "no")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "sugang_no", referencedColumnName = "no", nullable = false) // 관계 매핑
    private Sugang sugang;

    // studentNo 필드를 제거하고, getStudentNo() 메서드를 추가하여 student의 no 값을 가져옴
    public String getStudentNo() {
        return student != null ? String.valueOf(student.getNo()) : null;
    }
}
