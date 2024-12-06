package com.project.hit.Attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/Attendance/AttendanceCalendar")
    public String showAttendanceCalendar() {
        return "Attendance/AttendanceCalendar";
    }


    @GetMapping("Attendance/date")
    @ResponseBody
    public List<Attendance> getAttendanceData(@RequestParam String date) {
        List<Attendance> attendanceData = attendanceService.getAttendanceByDate(date);
        System.out.println("Data Sent for Date: " + date + ", Result: " + attendanceData); // 로그 추가
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
    public String showAttendanceCalendarStudent() {
        return "Attendance/AttendanceCalendarStudent";
    }
}
