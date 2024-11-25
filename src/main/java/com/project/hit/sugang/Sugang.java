package com.project.hit.sugang;

import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Sugang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    private LocalDateTime reg_date;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int present;                        // 출석 점수

    @Column(columnDefinition = "INT DEFAULT 0")
    private int semi_score;                     // 중간고사 점수

    @Column(columnDefinition = "INT DEFAULT 0")
    private int final_score;                    // 기말고사 점수

    @Column(columnDefinition = "INT DEFAULT 0")
    private int assignment1;                    // 과제1

    @Column(columnDefinition = "INT DEFAULT 0")
    private int assignment2;                    // 과제2
}
