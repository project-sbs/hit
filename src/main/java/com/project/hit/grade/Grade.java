package com.project.hit.grade;

import com.project.hit.student.Student;
import com.project.hit.sugang.Sugang;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int total_point;                                                                                            //ex) 100, 90, 80등 실 점수

    private String grade;                                                                                               //ex) A+, A, B+, B 등 등급

    @Column(columnDefinition = "DECIMAL(3, 2) DEFAULT 0.0")
    private double score;                                                                                               //ex) 4.5, 4.0, 3.5 등 평점

    private String year;

    private String semester;

    @ManyToOne
    private Student student;

    @OneToOne
    private Sugang sugang;
}
