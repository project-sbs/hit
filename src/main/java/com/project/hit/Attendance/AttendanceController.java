package com.project.hit.Attendance;

import com.project.hit.student.Student;
import com.project.hit.student.StudentService;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.hit.Attendance.AttendanceController.SemesterUtil.getSemester;

@Controller
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final SugangService sugangService;
    private final StudentService studentService;

    @GetMapping("/Attendance/AttendanceCalendar")
    public String showAttendanceCalendar() {
        return "Attendance/AttendanceCalendar";
    }



    public static class SemesterUtil {
        public static String getSemester(int month) {
            if (month >= 1 && month <= 6) {
                return "semester1";
            } else {
                return "semester2";
            }
        }
    }


    @GetMapping("/Attendance/date")
    @ResponseBody
    public List<AttendanceDTO> getAttendanceData(@RequestParam String date, HttpServletRequest request) {
        // 세션에서 사용자 역할과 ID 가져오기
        String userRole = (String) request.getSession().getAttribute("userRole");
        System.out.println("userRole: " + userRole);
        String studentId = (String) request.getSession().getAttribute("studentId");
        System.out.println("Student_id: " + studentId);

        // 역할이 학생(STUDENT)인 경우 본인의 출석 데이터만 반환
        if ("STUDENT".equals(userRole)) {
            List<AttendanceDTO> attendanceData = attendanceService.getAttendanceByStudentAndDate(studentId, date);
            System.out.println("학생 조회 - 날짜: " + date + ", 데이터 크기: " + attendanceData.size());
            return attendanceData;
        }

        List<AttendanceDTO> attendanceData = attendanceService.getAttendanceByDate(date);
        //System.out.println("데이트 : " + date);
        //System.out.println("데이터 사이즈 : " + attendanceData.size());
        return attendanceData;
    }

    @PostMapping("Attendance/save")
    @ResponseBody
    public String saveAttendance(@RequestBody List<AttendanceUpdateRequest> updates) {
        System.out.println("Received attendance updates: " + updates);
        for (AttendanceUpdateRequest update : updates) {
            attendanceService.updateAttendanceStatus(update.getStudentId(), update.getAttendanceStatus());

        }
        return "출석상태가 저장되었습니다";
    }



    @GetMapping("/Attendance/AttendanceCalendarStudent")
    public String getAttendanceStudent(Model model, Principal principal) {

        Student student = this.studentService.getStudentById(principal.getName());
        System.out.println("학생 ID: " + student.getId());

        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);

        List<Sugang> sugangList = this.sugangService.getCurrentSugangs(student, semester, year);

        try {
            attendanceService.createAttendanceSugang(student, sugangList); // 출석 데이터를 생성하는 메서드 호출
        } catch (Exception e) {
            System.out.println("출석 데이터 생성 중 오류 발생: " + e.getMessage());
        }

        model.addAttribute("student", student);
        model.addAttribute("sugangList", sugangList);
        return "Attendance/AttendanceCalendarStudent";
    }




}
