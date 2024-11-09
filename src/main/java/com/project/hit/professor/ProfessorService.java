package com.project.hit.professor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    public Professor addProfessor(Professor professor) {
        this.professorRepository.save(professor);
        return professor;
    }

}
