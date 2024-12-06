package com.project.hit.student;

import com.project.hit.board.Board;
import com.project.hit.board.BoardService;
import com.project.hit.major.Major;
import com.project.hit.major.MajorService;
import com.project.hit.subject.Subject;
import com.project.hit.subject.SubjectService;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/s")
public class StudentController {

    private final StudentService studentService;
    private final SugangService sugangService;
    private final MajorService majorService;
    private final SubjectService subjectService;
    private final BoardService boardService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        List<Board> noticeList = this.boardService.getTop6Boards("notice");
        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);

        List<Sugang> sugangList = this.sugangService.getCurrentSugangs(student, semester, year);

        model.addAttribute("student", student);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("sugangList", sugangList);
        return "portal/student/student_home";
    }

    @GetMapping("/info")   // 학생
    public String info(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());

        model.addAttribute("student", student);
        return "portal/student/student_info";
    }

    @GetMapping("/score")
    public String score(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());

        model.addAttribute("student", student);
        return "portal/student/student_score";
    }

    @GetMapping("/report")
    public String report(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());

        model.addAttribute("student", student);
        return "portal/student/student_report";
    }

    @GetMapping("/course")
    public String course(@RequestParam(name = "major", defaultValue = "전체") String major, @RequestParam(name = "department", defaultValue = "0") int department,
                         @RequestParam(name = "page", defaultValue = "0") int page, Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);

        List<Major> majorList = this.majorService.getAllMajors();
        Page<Subject> subjectList = getSubjectList(year, semester, major, department, page);
        List<Sugang> sugangList = this.sugangService.getCurrentSugangs(student, semester, year);
        int totalCredit = getTotalCredit(sugangList);
        List<Integer> appliedSubjects = sugangList.stream().map(s -> s.getSubject().getNo()).toList();

        int totalPage = subjectList.getTotalPages();
        int block = 5;
        int currentPage = subjectList.getNumber() + 1;

        int[] pageBlock = getPageBlock(totalPage, currentPage, block);
        int startBlock = pageBlock[0];
        int endBlock = pageBlock[1];

        model.addAttribute("sugangList", sugangList);
        model.addAttribute("totalCredit", totalCredit);
        model.addAttribute("appliedSubjects", appliedSubjects);
        model.addAttribute("student", student);
        model.addAttribute("majorList", majorList);
        model.addAttribute("department", department);
        model.addAttribute("major", major);
        model.addAttribute("page", page);
        model.addAttribute("subjectList", subjectList);
        model.addAttribute("startBlock", startBlock);
        model.addAttribute("endBlock", endBlock);

        return "portal/student/student_courseChoice";
    }

    /*@PostMapping("/insert/course")
    public String insertCourse(@RequestParam("selectedNo") List<Integer> selectedNo, @RequestParam("major") String major,
                               @RequestParam("department") int department, @RequestParam("page") int page, Principal principal,
                               RedirectAttributes redirectAttr) {
        Student student = this.studentService.getStudentById(principal.getName());
        this.sugangService.insertSugang(selectedNo, student);

        redirectAttr.addAttribute("major", major);
        redirectAttr.addAttribute("department", department);
        redirectAttr.addAttribute("page", page);

        return "redirect:/s/course";
    }*/

    @PostMapping("/insert/course")
    public ResponseEntity<String> insertCourse(@RequestBody List<Integer> selectedNo, Principal principal) {

        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);

        Student student = this.studentService.getStudentById(principal.getName());
        List<Sugang> sugangList = this.sugangService.getCurrentSugangs(student, semester, year);

        boolean fullCheck = this.sugangService.isCourseFull(selectedNo);
        boolean timeCheck = this.sugangService.isTimeOverLap(selectedNo, sugangList);
        boolean applyTimeCheck = this.sugangService.applyListTimeCheck(selectedNo);
        if (fullCheck || timeCheck || applyTimeCheck) {
            if (fullCheck) {
                return ResponseEntity.badRequest().body("이미 정원이 초과된 과목이 포함되었습니다.");
            } else if (timeCheck) {
                return ResponseEntity.badRequest().body("이미 신청한 시간과 중복되는 과목이 포함되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("시간이 중복되는 과목이 포함되었습니다.");
            }
        }

        this.sugangService.insertSugang(selectedNo, student);
        return ResponseEntity.ok().body("수강신청이 완료되었습니다.");
    }

    @GetMapping("/delete/course/{no}")
    @ResponseBody
    public ResponseEntity<String> deleteCourse(@PathVariable("no") int no, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        boolean isDeleted = this.sugangService.deleteSugang(no, student);

        if (isDeleted) {
            return ResponseEntity.ok("삭제 성공.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 오류.");
        }
    }

    private String getSemester(int month) {
        String semester;
        switch (month) {
            case 3: case 4: case 5: case 6:
                semester = "semester1";
                break;
            case 9: case 10: case 11: case 12:
                semester = "semester2";
                break;
            default:
                semester = "계절학기";
                break;
        }
        return semester;
    }

    private int getTotalCredit(List<Sugang> sugangList) {
        int totalCredit = 0;
        for (Sugang sugang : sugangList) {
            totalCredit += sugang.getSubject().getCredits();
        }
        return totalCredit;
    }

    private Page<Subject> getSubjectList(String year, String semester, String major, int department, int page) {
        if (major.equals("전체")) {
            return this.subjectService.getAllSubjects(year, semester, page);
        }
        return this.subjectService.getSubjectList(year, semester, major, department, page);
    }

    private int[] getPageBlock(int totalPages, int currentPage, int block) {
        int startBlock = (((currentPage - 1) / block) * block) + 1;
        int endBlock = startBlock + block - 1;

        if (endBlock > totalPages) {
            endBlock = totalPages;
        }
        return new int[] { startBlock, endBlock };
    }
}
