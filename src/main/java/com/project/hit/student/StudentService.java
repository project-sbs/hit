package com.project.hit.student;

import com.project.hit.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Student> getAllStudents() {return this.studentRepository.findAll();}

    public Student getStudentById(String id) {
        Optional<Student> student = this.studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new DataNotFoundException("Student not found for " + id);
        }
    }
}
