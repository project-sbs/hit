package com.project.hit.major;

import com.project.hit.professor.Professor;
import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int totalStudent;

    private String chair;

    @OneToMany(mappedBy = "major")
    private List<Student> students;

    @OneToMany(mappedBy = "major")
    private List<Professor> professors;

    @OneToMany(mappedBy = "major")
    private List<Subject> subjects;
}
