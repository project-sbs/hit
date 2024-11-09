package com.project.hit.professor;

import com.project.hit.major.Major;
import com.project.hit.subject.Subject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;                                    // 고유번호(PK)

    @Column(unique=true)
    private String id;                                  // 학번

    private String name;                                // 이름

    private String password;                            // 비밀번호

    @Column(unique=true)
    private String email;                               // 이메일

    private String phone;                               // 연락처

    private String birthday;                            // 생년월일

    // private String gender;                              // 성별

    private String ROLE;                                // 역할

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "professor")
    private List<Subject> subjects;
}
