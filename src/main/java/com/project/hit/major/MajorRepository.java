package com.project.hit.major;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findById(int major);

    Major findByName(String name);

    @Query("select distinct m from Major m order by m.id desc")
    List<Major> findById();

    Page<Major> findAll(Pageable pageable);
}
