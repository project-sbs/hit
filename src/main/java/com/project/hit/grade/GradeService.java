package com.project.hit.grade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;

    @Transactional
    public void updateGrade(int subject_no) {
        List<Grade> gradeList = this.gradeRepository.findGradeBySubject(subject_no);
        gradeList.sort((g1, g2) -> g2.getTotal_point() - g1.getTotal_point());      // total_point 기준 내림차순 정렬.

        settingGrade(gradeList);
        this.gradeRepository.saveAll(gradeList);
    }

    private void settingGrade(List<Grade> gradeList) {

        int totalStudents = gradeList.size();
        int aPlus = (int) Math.ceil(totalStudents * 0.05); // 상위 5%
        int aZero = (int) Math.ceil(totalStudents * 0.15); // 상위 15%
        int bPlus = (int) Math.ceil(totalStudents * 0.1); // 다음 10%
        int bZero = (int) Math.ceil(totalStudents * 0.2); // 다음 20%
        int cPlus = (int) Math.ceil(totalStudents * 0.1); // 다음 10%
        int cZero = (int) Math.ceil(totalStudents * 0.2); // 다음 20%
        int dPlus = (int) Math.ceil(totalStudents * 0.05); // 하위 5%
        int dZero = (int) Math.ceil(totalStudents * 0.15); // 하위 15%

        // 동점자 처리를 위해 등급별 목표 인원 수를 조정
        int currentRank = 0;
        for (int i = 0; i < gradeList.size(); ) {
            int start = i;
            int point = gradeList.get(i).getTotal_point();

            // 동점자 그룹 확인
            while (i < gradeList.size() && gradeList.get(i).getTotal_point() == point) {
                i++;
            }
            int cnt = i - start; // 동점자 수

            // 동점자 수에 맞게 각 등급을 할당
            if (currentRank < aPlus) {
                assignGradeToGroup(gradeList, start, i, "A+", 4.5);
                aPlus -= cnt;
            } else if (currentRank < aZero) {
                assignGradeToGroup(gradeList, start, i, "A0", 4.0);
                aZero -= cnt;
            } else if (currentRank < bPlus) {
                assignGradeToGroup(gradeList, start, i, "B+", 3.5);
                bPlus -= cnt;
            } else if (currentRank < bZero) {
                assignGradeToGroup(gradeList, start, i, "B0", 3.0);
                bZero -= cnt;
            } else if (currentRank < cPlus) {
                assignGradeToGroup(gradeList, start, i, "C+", 2.5);
                cPlus -= cnt;
            } else if (currentRank < cZero) {
                assignGradeToGroup(gradeList, start, i, "C0", 2.0);
                cZero -= cnt;
            } else if (currentRank < dPlus) {
                assignGradeToGroup(gradeList, start, i, "D+", 1.5);
                dPlus -= cnt;
            } else if (currentRank < dZero) {
                assignGradeToGroup(gradeList, start, i, "D0", 1.0);
                dZero -= cnt;
            } else {
                assignGradeToGroup(gradeList, start, i, "F", 0.0);
            }

            currentRank += cnt; // 순위 업데이트
        }
    }

    private static void assignGradeToGroup(List<Grade> gradeList, int start, int end, String grade, double score) {
        for (int i = start; i < end; i++) {
            gradeList.get(i).setGrade(grade);
            gradeList.get(i).setScore(score);
        }
    }

}
