package com.project.hit.professor;

import com.project.hit.DataNotFoundException;
import com.project.hit.major.Major;
import com.project.hit.major.MajorRepository;
import com.project.hit.major.MajorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final MajorRepository majorRepository;
    private final MajorService majorService;
    private final ModelMapper modelMapper = new ModelMapper();

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
        return this.professorRepository.findAllByOrderByNoDesc(pageable);
    }

    public void updateProfessor(Professor professor) {
        Optional<Professor> prof = this.professorRepository.findById(professor.getId());
        if (prof.isPresent()) {
            Professor temp = prof.get();
            temp.setName(professor.getName());
            temp.setMajor(professor.getMajor());
            temp.setBirthday(professor.getBirthday());
            temp.setPhone(professor.getPhone());
            if(!temp.getEmail().equals(professor.getEmail())) {
                temp.setEmail(professor.getEmail());
            }
            temp.setBirthday(professor.getBirthday());
            if ("학과장".equals(temp.getROLE()) || "학과장".equals(professor.getROLE())) {
                if ("학과장".equals(temp.getROLE())) {
                    this.majorService.updateChairMan(professor.getMajor());
                } else { // 교수의 역할이 학과장인 경우
                    this.majorService.updateChairMan(professor.getMajor(), professor.getName());
                }
            }
            temp.setROLE(professor.getROLE());
            this.professorRepository.save(temp);
        } else {
            throw new DataNotFoundException("Professor not found for " + professor.getId());
        }
    }

    public Professor getProfessor(long professorNo) {
        Optional<Professor> professor = this.professorRepository.findById(professorNo);
        if (professor.isPresent()) {
            return professor.get();
        } else {
            throw new DataNotFoundException("Not found for professor ID: " + professorNo);
        }
    }

    public Professor getProfessor(String id) {
        Optional<Professor> _professor = this.professorRepository.findById(id);
        if (_professor.isPresent()) {
            return _professor.get();
        } else {
            throw new DataNotFoundException("Not found for professor ID: " + id);
        }
    }

    public List<ProfessorDTO> getProfessorsByMajor(Major major) {
        List<Professor> professors = this.professorRepository.findProfessorByMajor(major);
        return professors.stream().map(p -> modelMapper.map(p, ProfessorDTO.class)) .collect(Collectors.toList());
    }

    public List<ProfessorDTO> getProfessorAllList() {
        List<Professor> professors = this.professorRepository.findAll();
        return professors.stream().map(p -> modelMapper.map(p, ProfessorDTO.class)) .collect(Collectors.toList());
    }
}
