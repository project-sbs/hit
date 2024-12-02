package com.project.hit.sugang;

import com.project.hit.DataNotFoundException;
import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import com.project.hit.subject.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SugangService {
    private final SugangRepository sugangRepository;
    private final SubjectRepository subjectRepository;

    public List<Sugang> getCurrentSugangs(Student student, String semester, String year) {
        return this.sugangRepository.findCurrentSubjectList(student, semester, year);
    }

    public void insertSugang(List<Integer> subjectIds, Student student) {
        for(int no : subjectIds) {
            Optional<Subject> _subject = subjectRepository.findById(no);
            if(_subject.isPresent()) {
                Sugang sugang = new Sugang();
                LocalDateTime today = LocalDateTime.now();
                sugang.setStudent(student);
                sugang.setReg_date(today);
                sugang.setSubject(_subject.get());
                this.sugangRepository.save(sugang);
            } else {
                throw new IllegalArgumentException("Subject not found");
            }
        }
    }

    @Transactional
    public boolean deleteSugang(int subjectId, Student student) {
        Sugang sugang = new Sugang();
        sugang.setStudent(student);
        Optional<Subject> _subject = subjectRepository.findById(subjectId);
        if(_subject.isPresent()) {
            sugang.setSubject(_subject.get());
            this.sugangRepository.deleteSugangBySubjectAndStudent(_subject.get(), student);
            return true;
        } else {
            throw new DataNotFoundException("Subject not found");
        }
    }
}
