package com.project.hit.professor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorDTO {
    private Long no;                                    // 고유번호(PK)

    private String id;                                  // 학번

    private String name;                                // 이름

    private String email;                               // 이메일

    private String phone;                               // 연락처

    private String birthday;                            // 생년월일

    private String ROLE;                                // 역할

}
