package com.project.hit.professor;

import com.project.hit.DataNotFoundException;
import com.project.hit.major.Major;
import com.project.hit.major.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final MajorRepository majorRepository;

    public Professor addProfessor(Professor professor) {
        this.professorRepository.save(professor);
        return professor;
    }

    public void insertProfessor(Professor professor) {      // test 상의 더미 데이터 insert 시 사용하는 메서드
        this.professorRepository.save(professor);
    }

    public List<Professor> getAllProfessors() {return this.professorRepository.findAll();}

    public Page<Professor> getProfessors(String field, String keyword, int page, int major_id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (major_id > 0) {
            Optional<Major> maj = this.majorRepository.findById(major_id);
            if (maj.isPresent()) {
                Major major = maj.get();
                if (keyword != null && !keyword.isEmpty()) {
                    if (field.equals("이름")) {
                        return this.professorRepository.findProfessorByNameContainingAndMajor(keyword, major, pageable);
                    } else if (field.equals("사번")) {
                        return this.professorRepository.findProfessorByIdContainingAndMajor(keyword, major, pageable);
                    }
                }
                return this.professorRepository.findProfessorByMajor(major, pageable);
            } else {
                throw new DataNotFoundException("Major not found for " + major_id);
            }
        } else {
            if (keyword != null && !keyword.isEmpty()) {
                if (field.equals("이름") ) {
                    return this.professorRepository.findProfessorByName(keyword, pageable);
                } else if (field.equals("사번")) {
                    return this.professorRepository.findProfessorById(keyword, pageable);
                }
            }
        }
        return this.professorRepository.findAllByOrderByIdDesc(pageable);
    }
}
