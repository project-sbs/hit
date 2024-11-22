package com.project.hit.Attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/Attendance/AttendanceCalendar")
    public String showAttendanceCalendar(Authentication authentication, Model model) {
        boolean isProfessor = false;

        // 사용자 권한 확인
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            isProfessor = authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_교수"));
        }

        model.addAttribute("isProfessor", isProfessor); // 뷰에서 사용
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
