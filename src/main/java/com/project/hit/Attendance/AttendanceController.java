package com.project.hit.Attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
