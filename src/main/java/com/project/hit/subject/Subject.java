package com.project.hit.subject;

import com.project.hit.major.Major;
import com.project.hit.professor.Professor;
import com.project.hit.student.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;

    private String name;           // 과목명

    private String week;           // 날짜

    private String time;           // 시간

    private String grade;          // 등급

    private String maxPersonnel;   // 총 인원

    private int credits;           // 학점

    private String semester;       // 학기

    private String year;           // 년도

    private String liberal;        // 교양

    @ManyToOne
    @JoinColumn(name = "major")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "professor")
    private Professor professor;

    @ManyToOne
    private Student student;


}






