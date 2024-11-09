package com.project.hit.professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Query("select distinct pr from Professor pr where pr.id = :id")
    Optional<Professor> findById(@Param("id") String id);
}
