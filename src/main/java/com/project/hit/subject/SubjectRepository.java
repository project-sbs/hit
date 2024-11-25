package com.project.hit.subject;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.hit.subject.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, String> {

    @Query("select st from Subject st where st.code = :code")
    Optional<Subject> findByCode(@Param("code") String code);

    List<Subject> findAll();

}


