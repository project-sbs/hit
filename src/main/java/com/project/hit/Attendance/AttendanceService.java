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

    public void updateAttendanceStatus(Long studentId, String attendanceStatus) {
        Attendance attendance = attendanceRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생 ID를 찾을 수 없습니다: " + studentId));
        System.out.println("Found attendance: " + attendance);
        attendance.setAttendanceStatus(attendanceStatus); // 출석 상태 업데이트
        attendanceRepository.save(attendance); // 저장
    }
}
