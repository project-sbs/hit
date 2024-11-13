package com.project.hit.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorInsertForm {

    private String id;          // 학번

    private String name;        // 이름

    private String email;       // 이메일

    private String phone;       // 연락처

    private String birthday;    // 생년월일

    private String major;       // 학과

    private String role;      // 교수 직위 (교수만 지정, 학생은 학생으로 저장.)
}
