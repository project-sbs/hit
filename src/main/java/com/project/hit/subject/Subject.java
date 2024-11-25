package com.project.hit.subject;

import com.project.hit.major.Major;
import com.project.hit.professor.Professor;
import com.project.hit.sugang.Sugang;
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
    private int no;

    private String name;           // 과목명

    private String week;           // 날짜

    private String time;           // 시간

    private int maxPersonnel;        // 총 인원

    private int credits;           // 학점

    private String semester;       // 학기

    private String year;           // 년도

    private String type;            // 전공 or 교양

    @ManyToOne
    private Major major;

    @ManyToOne
    private Professor professor;

    @OneToMany(mappedBy = "subject")
    private List<Sugang> sugangList;


}






