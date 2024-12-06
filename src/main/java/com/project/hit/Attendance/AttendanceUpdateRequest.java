package com.project.hit.Attendance;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceUpdateRequest {
    private Long studentId;
    private String attendanceStatus;
}
