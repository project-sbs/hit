package com.project.hit.sugang;

import com.project.hit.DataNotFoundException;
import com.project.hit.grade.Grade;
import com.project.hit.grade.GradeRepository;
import com.project.hit.grade.GradeService;
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
    private final GradeRepository gradeRepository;
    private final GradeService gradeService;

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
                Sugang _sugang = this.sugangRepository.save(sugang);
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setYear(_sugang.getSubject().getYear());
                grade.setSemester(_sugang.getSubject().getSemester());
                grade.setSugang(_sugang);
                grade.setGrade("미반영");
                this.gradeRepository.save(grade);
            } else {
                throw new IllegalArgumentException("Subject not found");
            }
        }
    }

    public boolean isCourseFull(List<Integer> subjectIds) {
        for(int no : subjectIds) {
            Subject subject = this.subjectRepository.findByNo(no);
            if (subject.getMaxPersonnel() <= subject.getSugangList().size()) {
                return true;
            }
        }
        return false;
    }

    public boolean isTimeOverLap(List<Integer> subjectIds, List<Sugang> sugangList) {
        for(Sugang sugang : sugangList) {
            for(int no : subjectIds) {
                Subject subject = this.subjectRepository.findByNo(no);
                /*String[] range1 = sugang.getSubject().getTime().split("-");     // 이미 신청한 수강 리스트의 Start-End Time
                String[] range2 = subject.getTime().split("-");                 // 새로 신청할 수강과목의 Start-End Time

                String start1 = range1[0];
                String end1 = range1[1];
                String start2 = range2[0];
                String end2 = range2[1];

                String days1 = sugang.getSubject().getWeek();
                String days2 = subject.getWeek();

                if (isDaysOverlap(days1, days2)) {

                    java.time.LocalTime startTime1 = java.time.LocalTime.parse(start1);
                    java.time.LocalTime endTime1 = java.time.LocalTime.parse(end1);
                    java.time.LocalTime startTime2 = java.time.LocalTime.parse(start2);
                    java.time.LocalTime endTime2 = java.time.LocalTime.parse(end2);

                    if (startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1)) {
                        return true;
                    }
                }*/
                if (isDaysOverlap(sugang.getSubject().getWeek(), subject.getWeek())) {
                    if (isTimesOverlap(sugang.getSubject().getTime(), subject.getTime())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean applyListTimeCheck(List<Integer> subjectIds) {
        for(int i = 0 ; i < subjectIds.size() ; i++) {
            Subject subject1 = this.subjectRepository.findByNo(subjectIds.get(i));
            for (int j = i + 1; j < subjectIds.size(); j++) {
                Subject subject2 = this.subjectRepository.findByNo(subjectIds.get(j));

                if (isDaysOverlap(subject1.getWeek(), subject2.getWeek())) {
                    if (isTimesOverlap(subject1.getTime(), subject2.getTime())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 시간이 겹치는지 확인하는 메소드
    private boolean isTimesOverlap(String time1, String time2) {
        String[] range1 = time1.split("-");
        String[] range2 = time2.split("-");

        String start1 = range1[0];
        String end1 = range1[1];
        String start2 = range2[0];
        String end2 = range2[1];

        java.time.LocalTime startTime1 = java.time.LocalTime.parse(start1);
        java.time.LocalTime endTime1 = java.time.LocalTime.parse(end1);
        java.time.LocalTime startTime2 = java.time.LocalTime.parse(start2);
        java.time.LocalTime endTime2 = java.time.LocalTime.parse(end2);

        return (startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1));
    }

    // 요일이 겹치는지 확인하는 매서드.
    private boolean isDaysOverlap(String days1, String days2) {
        String[] daysArray1 = days1.split(",\\s");          // "월, 수, 금"    => ["월", "수", "금"]
        String[] daysArray2 = days2.split(",\\s");          // "화, 목"        => ["화", "목"]

        for (String day1 : daysArray1) {
            for (String day2 : daysArray2) {
                if (day1.equals(day2)) {
                    return true;
                }
            }
        }
        return false;
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

    public void updateSugang(Sugang sugang) {
        Sugang _sugang = this.sugangRepository.save(sugang);
        if (_sugang.getFinal_score() != 0) {        // 기말점수 입력 된 상태. -> 등급 update O
            Grade grade = this.gradeRepository.findGradeBySugang(_sugang);
            int total;
            if (_sugang.getFinal_score() > 0) {
                total = _sugang.getAssignment1() + _sugang.getAssignment2() + _sugang.getSemi_score() + _sugang.getFinal_score();
            } else {
                total = _sugang.getAssignment1() + _sugang.getAssignment2() + _sugang.getSemi_score();
            }
            grade.setTotal_point(total);
            this.gradeRepository.save(grade);
            this.gradeService.updateGrade(_sugang.getSubject().getNo());
        }
    }

    public Sugang getSugang(int no) {
        Optional<Sugang> _sugang = this.sugangRepository.findByNo(no);
        if(_sugang.isPresent()) {
            return _sugang.get();
        } else {
            throw new DataNotFoundException("Sugang not found No: " + no);
        }
    }

    public List<Sugang> getSugangList(Student student) {
        return this.sugangRepository.findSugangByStudent(student);
    }
}
