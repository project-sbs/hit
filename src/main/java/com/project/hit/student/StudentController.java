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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        model.addAttribute("student", student);
        model.addAttribute("noticeList", noticeList);
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
        Page<Subject> subjectList = getSubjectList(year, getSemester(month), major, department, page);
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

    @PostMapping("/insert/course")
    public String insertCourse(@RequestParam("selectedNo") List<Integer> selectedNo, @RequestParam("major") String major,
                               @RequestParam("department") int department, @RequestParam("page") int page, Principal principal,
                               RedirectAttributes redirectAttr) {
        Student student = this.studentService.getStudentById(principal.getName());
        this.sugangService.insertSugang(selectedNo, student);

        redirectAttr.addAttribute("major", major);
        redirectAttr.addAttribute("department", department);
        redirectAttr.addAttribute("page", page);

        return "redirect:/s/course";
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
