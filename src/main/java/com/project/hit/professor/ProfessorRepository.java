package com.project.hit.professor;

import com.project.hit.major.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Query("select distinct pr from Professor pr where pr.id = :id")
    Optional<Professor> findById(@Param("id") String id);

    Page<Professor> findAllByOrderByIdDesc(Pageable pageable);

    Page<Professor> findProfessorByMajor(Major major, Pageable pageable);

    @Query("select distinct pr from Professor pr where pr.name like %:keyword%")
    Page<Professor> findProfessorByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("select distinct pr from Professor pr where pr.id like %:keyword%")
    Page<Professor> findProfessorById(@Param("keyword") String keyword, Pageable pageable);

    Page<Professor> findProfessorByNameContainingAndMajor(String name, Major major, Pageable pageable);

    Page<Professor> findProfessorByIdContainingAndMajor(String id, Major major, Pageable pageable);
}
