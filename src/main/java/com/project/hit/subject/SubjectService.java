package com.project.hit.subject;

import com.project.hit.student.Student;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SubjectService {

    private final SubjectRepository subjectRepository;

    public Subject addSubject(Subject subject) {
        this.subjectRepository.save(subject);
        return subject;
    }

    public void insertSubject(Subject subject) {
        this.subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return this.subjectRepository.findAll();}
}
