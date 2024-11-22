package com.project.hit.Attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Attendance {

    @Id
    private Long id;
    private String studentName;
    private String attendanceStatus;
    private String date;

}
