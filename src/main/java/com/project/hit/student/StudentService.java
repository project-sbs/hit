package com.project.hit.student;

import com.project.hit.DataNotFoundException;
import com.project.hit.major.Major;
import com.project.hit.major.MajorRepository;
import com.project.hit.professor.Professor;
import com.project.hit.professor.ProfessorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public Student addStudent(Student student) {
        this.studentRepository.save(student);
        return student;
    }

    @Transactional
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

    public Page<Student> getStudents(String field, String keyword, int page, int major_id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("no"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (major_id > 0) {
            Optional<Major> maj = this.majorRepository.findById(major_id);
            if (maj.isPresent()) {
                Major major = maj.get();
                if (keyword != null && !keyword.isEmpty()) {
                    if (field.equals("이름") ) {
                        return this.studentRepository.findStudentByNameContainingAndMajor(keyword, major, pageable);
                    } else if (field.equals("학번")) {
                        return this.studentRepository.findStudentByIdContainingAndMajor(keyword, major, pageable);
                    }
                }
                return this.studentRepository.findStudentByMajor(major, pageable);
            } else {
                throw new DataNotFoundException("Major not found for " + major_id);
            }
        } else {
            if (keyword != null && !keyword.isEmpty()) {
                if (field.equals("이름") ) {
                    return this.studentRepository.findStudentByName(keyword, pageable);
                } else if (field.equals("학번")) {
                    return this.studentRepository.findStudentById(keyword, pageable);
                }
            }
        }
        return this.studentRepository.findAllByOrderByNoDesc(pageable);
    }

    @Transactional
    public void updateStudent(Student student) {
        Optional<Student> stud = this.studentRepository.findById(student.getId());
        if (stud.isPresent()) {
            Student temp = stud.get();
            temp.setMajor(student.getMajor());
            temp.setName(student.getName());
            temp.setBirthday(student.getBirthday());
            temp.setPhone(student.getPhone());
            if (!temp.getEmail().equals(student.getEmail())) {
                temp.setEmail(student.getEmail());
            }
            temp.setAddress(student.getAddress());
            temp.setBirthday(student.getBirthday());
            temp.setStatus(student.getStatus());
            this.studentRepository.save(temp);
            System.out.println("Student address updated: " + temp.getAddress());
        } else {
            throw new DataNotFoundException("Student not found for " + student.getId());
        }
    }

    @Transactional
    public void updatePassword(Student student) {
        Optional<Student> temp = this.studentRepository.findById(student.getId());
        if (temp.isPresent()) {
            Student _student = temp.get();
            _student.setPassword(student.getPassword());
            this.studentRepository.save(_student);
        } else {
            throw new DataNotFoundException("Student not found for " + student.getId());
        }
    }

    public boolean isMatchedIdAndEmail(String id, String email) {
        return this.studentRepository.existsByIdAndEmail(id, email);
    }

    @Transactional
    public void modifyPassword(Student student) {
        this.studentRepository.save(student);
    }


    // 학년계산 나중에 더 공부!!
    @Transactional
    public void calculateGradeYear(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생을 찾을 수 없습니다."));

        if (student.getId() != null && student.getId().length() >= 4) {
            int entryYear = Integer.parseInt(student.getId().substring(0, 4));
            int currentYear = LocalDate.now().getYear();
            int gradeYear = currentYear - entryYear + 1;

            if (gradeYear > 3) {
                gradeYear = 3;
            }
            student.setGradeYear(gradeYear);
            studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("학번이 유효하지 않습니다.");
        }
    }
}
