package com.project.hit.Attendance;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceByDate(String date) {
        List<Attendance> result = attendanceRepository.findAttendanceByDate(date);
        System.out.println("Fetched Data: " + result); // 로그로 확인
        return result;
    }
}
