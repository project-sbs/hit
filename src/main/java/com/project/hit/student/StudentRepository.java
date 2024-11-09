package com.project.hit.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select distinct st from Student st where st.id = :id")
    Optional<Student> findById(@Param("id") String id);

}
