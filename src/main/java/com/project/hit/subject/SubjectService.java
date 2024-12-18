package com.project.hit.subject;

import com.project.hit.DataNotFoundException;
import com.project.hit.major.Major;
import com.project.hit.major.MajorRepository;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangRepository;
import jakarta.transaction.Transactional;
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
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final MajorRepository majorRepository;
    private final SugangRepository sugangRepository;

    @Transactional
    public Subject addSubject(Subject subject) {
        this.subjectRepository.save(subject);
        return subject;
    }

    @Transactional
    public void insertSubject(Subject subject) {
        this.subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return this.subjectRepository.findAll();}

    public List<Subject> getSubjectList(String type) {
        return this.subjectRepository.findSubjectByTypeContainingOrderByNoDesc(type);
    }

    public Page<Subject> getSubjectList(String year, String semester, String type, int department, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("no"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        if (department > 0) {
            Optional<Major> _major = this.majorRepository.findById(department);
            if (_major.isPresent()) {
                Major major = _major.get();
                return this.subjectRepository.findSubjectByMajorAndSemesterContainingAndYearContaining(major, semester, year, pageable);
            } else {
                throw new DataNotFoundException("Not found major for id : " + department);
            }
        }
        return this.subjectRepository.findSubjectByTypeContainingAndSemesterContainingAndYearContaining(type, semester, year, pageable);
    }

    public Page<Subject> getAllSubjects(String year, String semester, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("no"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.subjectRepository.findAllBySemesterContainingAndYearContaining(semester, year, pageable);
    }

    public Page<Subject>getList(int page,int major_id){
        Pageable pageable = PageRequest.of(page,10);
        return this.subjectRepository.findAll(pageable);
    }

    public Page<Subject> getSubjectList(String type, int page, int major_id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("no"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return subjectRepository.findSubjectByTypeContaining(type, pageable);
    }

    @Transactional
    public void delete(Subject subject){
        this.subjectRepository.delete(subject);
    }

    @Transactional
    public void deleteSubject(Subject subject) {
        subjectRepository.delete(subject);
    }

    public Subject getSubjectById(int no) {
        return subjectRepository.findById(no).orElse(null);
    }

    public Subject getSubject(int no) {
        return this.subjectRepository.findByNo(no);
    }

    public Page<Sugang> getSubjectSugangs(Subject subject, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("no"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.sugangRepository.findSugangsBySubject(subject, pageable);
    }

}
