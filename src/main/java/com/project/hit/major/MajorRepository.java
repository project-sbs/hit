package com.project.hit.major;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    Optional<Major> findById(int major);

    Major findByName(String name);
}
