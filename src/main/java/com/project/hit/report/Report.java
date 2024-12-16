package com.project.hit.report;

import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    private String title;
    private String filePath;
    private LocalDateTime regDate;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;
}
