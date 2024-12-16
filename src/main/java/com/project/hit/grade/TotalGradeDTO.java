package com.project.hit.grade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TotalGradeDTO {
    private String year;
    private String semester;
    private int courseCredits;                  // 신청학점
    private int totalCredits;                   // 취득학점
    private double divScore;                       // 평균 평점   =>       (평점 x 학점) + (평점 x 학점) + ... / 취득학점
    private int divPoint;                        // 평균 실점
}
