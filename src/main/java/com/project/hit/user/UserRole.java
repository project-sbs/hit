package com.project.hit.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_관리자"),
    STUDENT("ROLE_학생"),
    PROFESSOR("ROLE_교수");

    UserRole(String value) {this.value = value;}

    private String value;
}
