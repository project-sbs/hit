package com.project.hit.professor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    public Professor addProfessor(Professor professor) {
        this.professorRepository.save(professor);
        return professor;
    }

    public void insertProfessor(Professor professor) {      // test 상의 더미 데이터 insert 시 사용하는 메서드
        this.professorRepository.save(professor);
    }

    public List<Professor> getAllProfessors() {return this.professorRepository.findAll();}
}
