package com.project.hit.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectInsertForm {

    private String year;                 // 년도

    private String semester;             // 학기

    private String type;              // 전공/교양

    private String major;                // 학과

    private String name;                 // 과목명

    private Long professorNo;             // 교수명

    private String time;                 // 시간

    private String week;                  // 요일

    private int credits;                 // 학점

    private int maxPersonnel;            // 총 인원

}
