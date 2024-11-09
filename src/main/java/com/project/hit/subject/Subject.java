package com.project.hit.subject;

import com.project.hit.major.Major;
import com.project.hit.professor.Professor;
import com.project.hit.student.Student;
import com.project.hit.sugang.Sugang;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Subject {
    @Id
    private String code;

    private String name;

    private String description;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "subject")
    private List<Sugang> sugangList;
}
