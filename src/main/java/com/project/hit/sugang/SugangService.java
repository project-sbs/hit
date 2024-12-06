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
}
