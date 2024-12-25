package com.project.hit.student;

import com.project.hit.major.Major;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query("select distinct st from Student st where st.id = :id")
    Optional<Student> findById(@Param("id") String id);

    Page<Student> findAllByOrderByNoDesc(Pageable pageable);

    Page<Student> findStudentByMajor(Major major, Pageable pageable);

    @Query("select distinct st from Student st where st.name like %:keyword%")
    Page<Student> findStudentByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("select distinct st from Student st where st.id like %:keyword%")
    Page<Student> findStudentById(@Param("keyword") String keyword, Pageable pageable);

    Page<Student> findStudentByNameContainingAndMajor(String name, Major major, Pageable pageable);

    Page<Student> findStudentByIdContainingAndMajor(String id, Major major, Pageable pageable);

    boolean existsByIdAndEmail(String id, String email);

    @Query("select count(st) from Student st where st.status in ('재학', '휴학')")
    long countByStudent();

    @Query("select count(st) from Student st where st.status = :status")
    long countBySpecificStatus(@Param("status") String status);



}

