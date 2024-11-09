package com.project.hit.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("select distinct a from Admin a where a.id = :id")
    Optional<Admin> findById(@Param("id") String id);
}
