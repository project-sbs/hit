package com.project.hit.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;                                    // 고유번호

    @Column(unique=true)
    private String id;                                  // admin

    private String name;                                // 이름

    private String password;                            // 비밀번호

    @Column(unique=true)
    private String email;                               // 이메일

    private String phone;                               // 연락처

    private String ROLE;                                // 역할
}
