package com.project.hit.Attendance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired

    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceByDate(String date) {
        List<Attendance> result = attendanceRepository.findAttendanceByDate(date);
        System.out.println("Fetched Data: " + result); // 로그로 확인
        return result;
    }
}
