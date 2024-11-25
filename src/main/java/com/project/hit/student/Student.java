package com.project.hit.student;

import com.project.hit.major.Major;
import com.project.hit.subject.Subject;
import com.project.hit.sugang.Sugang;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Student {
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

    private String ROLE;                                // 역할

    private String profile;                             // 프로필 저장경로

    @Column(columnDefinition = "INT DEFAULT 0")         // 이수학점
    private int credits;

    private String status;                              // 상태(재학, 휴학, 졸업)

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "student")
    private List<Sugang> sugangList;

}
