package com.project.hit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthcodeRepository extends JpaRepository<Authcode, String> {
    Authcode findByEmail(String email);
}
