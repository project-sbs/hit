package com.project.hit.sugang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SugangRepository extends JpaRepository<Sugang, Long> {

    @Query("select distinct sg from Sugang sg where sg.subject.semester like %:semester% and sg.subject.year like %:year% order by sg.reg_date desc")
    List<Sugang> findCurrentSubjectList(String semester, String year);
}
