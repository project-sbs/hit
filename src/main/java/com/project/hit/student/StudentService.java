package com.project.hit.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student addStudent(Student student) {
        this.studentRepository.save(student);
        return student;
    }

    public void insertStudent(Student student) {    // test 상의 더미 데이터 insert 시 사용하는 메서드
        this.studentRepository.save(student);
    }
}
