package com.project.hit.sugang;

import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Sugang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    private String semester;

    private String grade;
}
