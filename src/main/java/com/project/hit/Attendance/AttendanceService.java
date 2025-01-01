package com.project.hit.Attendance;


import com.project.hit.student.Student;
import com.project.hit.subject.Subject;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final SugangRepository sugangRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<AttendanceDTO> getAttendanceByDate(String date) {
        List<Attendance> result = attendanceRepository.findAttendanceByDate(date);
        //System.out.println("Fetched Data: " + result); // 로그로 확인
        return result.stream().map(a -> modelMapper.map(a, AttendanceDTO.class)).collect(Collectors.toList());
    }

    public void updateAttendanceStatus(Long studentId, String attendanceStatus) {
        Attendance attendance = attendanceRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생 ID를 찾을 수 없습니다: " + studentId));
        //System.out.println("Found attendance: " + attendance);
        attendance.setAttendanceStatus(attendanceStatus); // 출석 상태 업데이트
        attendanceRepository.save(attendance); // 저장
    }

    public List<AttendanceDTO> getAttendanceByStudentAndDate(String studentId, String date) {
        return attendanceRepository.findByStudentIdAndDate(studentId, date);
    }

    @Transactional
    public void createAttendanceSugang(Student student, List<Sugang> sugangList) {
        //System.out.println("학생 정보: " + student.getNo() + ", 이름: " + student.getName());

        int currentMonth = LocalDate.now().getMonthValue(); // 현재 월
        String semester = AttendanceController.SemesterUtil.getSemester(currentMonth);

        LocalDate semesterStartDate;
        LocalDate semesterEndDate;
        LocalDate currentDate = LocalDate.now(); // 현재 날짜

        if ("semester1".equals(semester)) {
            semesterStartDate = LocalDate.of(currentDate.getYear(), 3, 1); // 1학기 시작 날짜
            semesterEndDate = LocalDate.of(currentDate.getYear(), 6, 30); // 1학기 종료 날짜
        } else {
            semesterStartDate = LocalDate.of(currentDate.getYear(), 8, 1); // 2학기 시작 날짜
            semesterEndDate = LocalDate.of(currentDate.getYear(), 12, 20); // 2학기 종료 날짜
        }

        for (Sugang sugang : sugangList) {
            Subject subject = sugang.getSubject(); // 과목 정보 가져오기
            String daysOfWeek = subject.getWeek(); // 월, 수, 금 등 요일 정보 가져오기

            if (subject.getWeek() == null || subject.getWeek().isEmpty()) {
                continue; // 요일 정보가 없는 경우 스킵
            }



            String[] days = daysOfWeek.split(","); // 요일을 분리
            Sugang savedSugang = sugangRepository.save(sugang);

            for (String day : days) {
                day = day.trim(); // 공백 제거

                DayOfWeek targetDayOfWeek = convertDayToDayOfWeek(day); // 요일을 DayOfWeek로 변환
                LocalDate targetDate = semesterStartDate.with(TemporalAdjusters.nextOrSame(targetDayOfWeek));

                while (!targetDate.isAfter(semesterEndDate)) {
                    Attendance existingAttendance = attendanceRepository.findByStudentAndSubjectAndDate(student, subject, targetDate.toString());
                    if (existingAttendance != null) {
                        // 이미 해당 날짜에 출석 기록이 있으면 새로운 출석을 생성하지 않음
                        targetDate = targetDate.plusWeeks(1);  // 다음 주로 넘어가기
                        continue;
                    }
                    // 출석 정보 생성
                    Attendance attendance = new Attendance();
                    attendance.setStudent(student);
                    attendance.setAttendanceStatus("미확인");
                    attendance.setDate(targetDate.toString());
                    attendance.setDayOfWeek(day);
                    attendance.setSubject(subject);
                    attendance.setSugang(savedSugang);

                    attendanceRepository.save(attendance);

                    // 다음 주의 동일 요일로 이동
                    targetDate = targetDate.plusWeeks(1);
                }
            }

        }
    }


    public void createAttendanceSugang(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("학생 정보가 없습니다.");
        }
    }

    // 요일을 DayOfWeek로 변환
    private DayOfWeek convertDayToDayOfWeek(String day) {
        switch (day) {
            case "월": return DayOfWeek.MONDAY;
            case "화": return DayOfWeek.TUESDAY;
            case "수": return DayOfWeek.WEDNESDAY;
            case "목": return DayOfWeek.THURSDAY;
            case "금": return DayOfWeek.FRIDAY;
            case "토": return DayOfWeek.SATURDAY;
            case "일": return DayOfWeek.SUNDAY;
            default: throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    public void createInitialAttendance(Student student, Subject subject) {
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSubject(subject);
        attendance.setAttendanceStatus("미확인");
        attendance.setDate(String.valueOf(LocalDate.now())); // 초기 날짜 설정
        attendanceRepository.save(attendance);
    }


}
