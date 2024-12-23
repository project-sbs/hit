package com.project.hit.student;

import com.project.hit.board.Board;
import com.project.hit.board.BoardService;
import com.project.hit.grade.Grade;
import com.project.hit.grade.GradeService;
import com.project.hit.grade.TotalGradeDTO;
import com.project.hit.major.Major;
import com.project.hit.major.MajorService;
import com.project.hit.report.Report;
import com.project.hit.report.ReportService;
import com.project.hit.subject.Subject;
import com.project.hit.subject.SubjectService;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/s")
public class StudentController {

    private final StudentService studentService;
    private final SugangService sugangService;
    private final MajorService majorService;
    private final SubjectService subjectService;
    private final BoardService boardService;
    private final GradeService gradeService;
    private final ReportService reportService;

    @Value("${upload.dir}/report/")
    private String uploadDir;

    private void createUploadDir() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "3") int size) {

        int totalSchedulers = this.boardService.getTotalSchedulersCount();
        int totalPages = (int) Math.ceil((double) totalSchedulers / size);

        if (page >= totalPages) {
            page = totalPages - 1;
        }
        if (page < 0) {
            page = 0;
        }

        LocalDate currentDate = LocalDate.now();
        Student student = this.studentService.getStudentById(principal.getName());
        List<Board> noticeList = this.boardService.getTop6Boards("notice");
        List<Board> educations = this.boardService.getTop6Boards("edu");
        List<Board> freebulletins = this.boardService.getTop6Boards("free");
        List<Board> jobpostings = this.boardService.getTop6Boards("hire");
        List<Board> schedulersa = this.boardService.getTop3Schedulers("scheduler");
        List<Board> notices = this.boardService.getTop6Boards("notice");
        List<Board> schedulers = this.boardService.getSchedulersByPage(page, size);
        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);
        int totalCredits = 0;

        List<Sugang> sugangList = this.sugangService.getCurrentSugangs(student, semester, year);

        for (Sugang sugang : sugangList) {
            totalCredits += sugang.getSubject().getCredits();
        }

        model.addAttribute("page", page);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("schedulers", schedulers);
        model.addAttribute("jobpostings", jobpostings);
        model.addAttribute("freebulletins", freebulletins);
        model.addAttribute("educations", educations);
        model.addAttribute("student", student);
        model.addAttribute("notices", notices);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("sugangList", sugangList);
        model.addAttribute("totalCredits", totalCredits);

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
        List<String[]> yearAndSemesters = this.gradeService.getYearAndSemester(student);
        List<TotalGradeDTO> totalGradeDTOList = this.gradeService.getTotalGrade(yearAndSemesters, student);
        List<Sugang> sugangList = this.sugangService.getSugangList(student);
        List<Grade> gradeList = this.gradeService.getGradeList(student);
        int courseCredits = 0;
        int totalCredits = 0;
        double percentage = 0;
        for (TotalGradeDTO totalGradeDTO : totalGradeDTOList) {
            courseCredits += totalGradeDTO.getCourseCredits();
            totalCredits += totalGradeDTO.getTotalCredits();
            percentage += totalGradeDTO.getDivScore();
        }
        percentage = (percentage / totalGradeDTOList.size()) * 10 + 54;

        model.addAttribute("student", student);
        model.addAttribute("totalGradeDTOList", totalGradeDTOList);
        model.addAttribute("sugangList", sugangList);
        model.addAttribute("gradeList", gradeList);
        model.addAttribute("courseCredits", courseCredits);
        model.addAttribute("totalCredits", totalCredits);
        model.addAttribute("percentage", percentage);
        return "portal/student/student_score";
    }


    @GetMapping("/report")
    public String report(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);
        List<Sugang> sugangList = this.sugangService.getCurrentSugangs(student, semester, year);

        model.addAttribute("student", student);
        model.addAttribute("sugangList", sugangList);
        return "portal/student/student_report";
    }

    @PostMapping("/report/insert")
    @ResponseBody
    public Map<String, String> insertReport(@RequestParam("files") List<MultipartFile> files, @RequestParam("title") String title,
                                            @RequestParam("selectNo") int selectNo , Principal principal) {
        Map<String, String> response = new HashMap<>();
        createUploadDir();
        try {
            Student student = this.studentService.getStudentById(principal.getName());
            Subject subject = this.subjectService.getSubject(selectNo);
            String folderPath = subject.getNo() + "/" + UUID.randomUUID();
            File folder = new File(uploadDir + folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(folder.getAbsolutePath(), fileName);
                Files.write(filePath, file.getBytes());
            }
            Report report = new Report();

            report.setTitle(title);
            report.setStudent(student);
            report.setSubject(subject);
            report.setFilePath(folder.getAbsolutePath());
            report.setRegDate(LocalDateTime.now());

            response.put("status", "success");
            response.put("message", "저장되었습니다.");
            response.put("redirectUrl", "/s/report");

            this.reportService.insertReport(report);
        } catch (Exception e) {
            response.put("error", "error");
            response.put("message", e.getMessage());
        }

        return response;
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

    @GetMapping("/board")
    public String board(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        List<Board> notices = this.boardService.getTop6Boards("notice");
        List<Board> educations = this.boardService.getTop6Boards("edu");
        List<Board> freebulletins = this.boardService.getTop6Boards("free");
        List<Board> jobpostings = this.boardService.getTop6Boards("hire");
        List<Board> contents = this.boardService.getTop6Boards("con");
        List<Board> schedulers = this.boardService.getTop3Schedulers("scheduler");

        model.addAttribute("schedulers", schedulers);
        model.addAttribute("contents", contents);
        model.addAttribute("jobpostings", jobpostings);
        model.addAttribute("freebulletins", freebulletins);
        model.addAttribute("notices", notices);
        model.addAttribute("educations", educations);
        model.addAttribute("student", student);
        return "portal/student/student_board";
    }

    @GetMapping("/detail/{id}")
    public String detail(Principal principal, Model model, @PathVariable("id") Long no) {
        Board board = this.boardService.getBoard(no);
        String type = board.getType();
        Board previousBoard = this.boardService.getPreviousBoard(no, type);
        Board nextBoard = this.boardService.getNextBoard(no, type);

        model.addAttribute("nextBoard",nextBoard);
        model.addAttribute("previousBoard",previousBoard);
        model.addAttribute("board", board);
        return "portal/student/student_detail";
    }

    @GetMapping("/check/pwd")
    public String checkPwd(Principal principal, Model model) {
        Student student = this.studentService.getStudentById(principal.getName());
        model.addAttribute("student", student);
        return "portal/student/student_password_check";
    }

    @PostMapping("/check/pwd")
    @ResponseBody
    public Map<String, String> checkPwd(Principal principal, @RequestParam("password") String password) {
        Student student = this.studentService.getStudentById(principal.getName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Map<String, String> response = new HashMap<>();
        if (student == null) {
            response.put("message", "일치하는 정보를 찾을수 없습니다.");

        } else if (passwordEncoder.matches(password, student.getPassword())) {
            response.put("status", "success");
            response.put("message", "일치하는 비밀번호입니다.");
            response.put("redirectUrl", "/s/modify/pwd");

        } else {
            response.put("message", "비밀번호가 일치하지 않습니다.");
        }
        return response;
    }

    @GetMapping("/modify/pwd")
    public String modifyPwd(Principal principal, Model model) {
        Student student = this.studentService.getStudentById(principal.getName());
        model.addAttribute("student", student);
        return "portal/student/student_password_modify";
    }

    @PostMapping("/modify/pwd")
    @ResponseBody
    public Map<String, String> modifyPwd(Principal principal, @RequestParam("password") String password, Model model) {
        Student student = this.studentService.getStudentById(principal.getName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Map<String, String> response = new HashMap<>();
        if (student == null) {
            response.put("message", "일치하는 정보를 찾을 수 없습니다.");
        } else {
            student.setPassword(passwordEncoder.encode(password));
            this.studentService.updatePassword(student);
            response.put("status", "success");
            response.put("message", "비밀번호를 변경했습니다. 재로그인 해주세요.");
            response.put("redirectUrl", "/logout");
        }
        return response;
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
