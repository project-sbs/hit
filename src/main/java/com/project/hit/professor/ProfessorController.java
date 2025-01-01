package com.project.hit.professor;

import com.project.hit.FileData;
import com.project.hit.admin.Admin;
import com.project.hit.board.Board;
import com.project.hit.board.BoardService;
import com.project.hit.report.Report;
import com.project.hit.report.ReportService;
import com.project.hit.student.StudentService;
import com.project.hit.subject.Subject;
import com.project.hit.subject.SubjectService;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangDTO;
import com.project.hit.sugang.SugangService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/p")
public class ProfessorController {

    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final SugangService sugangService;
    private final ReportService reportService;
    private final BoardService boardService;

    @GetMapping("/home")
    public String home(Principal principal, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "3") int size){

        int totalSchedulers = this.boardService.getTotalSchedulersCount();
        int totalPages = (int) Math.ceil((double) totalSchedulers / size);

        if (page >= totalPages) {
            page = totalPages - 1;
        }
        if (page < 0) {
            page = 0;
        }

        LocalDate currentDate = LocalDate.now();
        Professor professor = this.professorService.getProfessor(principal.getName());
        List<Board> notices = this.boardService.getTop6Boards("notice");
        List<Board> educations = this.boardService.getTop6Boards("edu");
        List<Board> freebulletins = this.boardService.getTop6Boards("free");
        List<Board> jobpostings = this.boardService.getTop6Boards("hire");
        List<Board> contents = this.boardService.getTop6Boards("con");
        List<Board> schedulers = this.boardService.getSchedulersByPage(page, size);
        List<Board> schedulersa = this.boardService.getTop3Schedulers("scheduler");

        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);



        // 교수의 과목 리스트 가져오기
        List<Subject> subjects = this.professorService.getProfessorSubjects(professor, year, semester);



        // 점심 메뉴 리스트
        List<String> lunchMenu = Arrays.asList(
                "불고기", "김치찌개", "된장찌개", "비빔밥", "떡볶이",
                "돈까스", "김밥", "제육덮밥", "닭갈비", "짜장면"
        );

        // 점심 메뉴 랜덤 4개 선택
        Collections.shuffle(lunchMenu);
        List<String> randomLunchMenu = lunchMenu.subList(0, 4);

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("notices", notices);
        model.addAttribute("educations", educations);
        model.addAttribute("freebulletins", freebulletins);
        model.addAttribute("jobpostings", jobpostings);
        model.addAttribute("contents", contents);
        model.addAttribute("schedulers", schedulers);
        model.addAttribute("professor",professor);
        model.addAttribute("page", page);
        model.addAttribute("subjects", subjects);
        model.addAttribute("lunchMenu", randomLunchMenu);

        return "portal/professor/professor_home";
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
        return "portal/professor/professor_detail";
    }

    @GetMapping("/info")
    public String info() {return "portal/professor/professor_info";}

    @GetMapping("/report")
    public String report(@RequestParam(name = "subjectNo", defaultValue = "0") int subjectNo, @RequestParam(name = "page", defaultValue = "0") int page,
                         Principal principal, Model model) {
        Professor professor = this.professorService.getProfessor(principal.getName());
        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);
        List<Subject> subjectList = this.professorService.getProfessorSubjects(professor, year, semester);
        if (subjectNo == 0) {
            Subject _subject = this.professorService.getFirstProfessorSubject(professor, year, semester);
            if (_subject != null) {
                subjectNo = _subject.getNo();
            } else {
                boolean isEmpty = true;
                model.addAttribute("isEmpty", isEmpty);
                model.addAttribute("professor", professor);
                return "portal/professor/professor_report_check";
            }
        }
        Subject subject = this.subjectService.getSubject(subjectNo);
        Page<Report> reportList = this.reportService.getReportsPagingList(subject, page);
        boolean isEmpty = false;
        if (reportList.isEmpty()) {
            isEmpty = true;
        }
        Map<Integer, List<FileData>> fileDataMap = new HashMap<>();

        for (Report report : reportList.getContent()) {
            File path = new File(report.getFilePath());
            File[] files = path.listFiles();
            List<FileData> fileDataList = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    try {
                        String encodedName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8).replace("+", "%20");
                        String decodedName = URLDecoder.decode(file.getName(), StandardCharsets.UTF_8);
                        fileDataList.add(new FileData(encodedName, decodedName));
                    } catch (Exception e) {
                        throw new RuntimeException("URL Encoding/Decoding failed!", e);
                    }
                }
            }
            fileDataMap.put(report.getNo(), fileDataList);
        }

        int totalPage = reportList.getTotalPages();
        int block = 5;
        int currentPage = reportList.getNumber() + 1;

        int[] pageBlock = getPageBlock(totalPage, currentPage, block);
        int startBlock = pageBlock[0];
        int endBlock = pageBlock[1];

        model.addAttribute("professor", professor);
        model.addAttribute("subjectList", subjectList);
        model.addAttribute("subjectNo", subjectNo);
        model.addAttribute("reportList", reportList);
        model.addAttribute("page", page);
        model.addAttribute("fileDataMap", fileDataMap);
        model.addAttribute("startBlock", startBlock);
        model.addAttribute("endBlock", endBlock);
        model.addAttribute("isEmpty", isEmpty);
        return "portal/professor/professor_report_check";
    }

    @GetMapping("/report/download")
    public ResponseEntity<Resource> download(@RequestParam("no") int no, @RequestParam("filename") String filename) {
        Report report = this.reportService.getReportByNo(no);
        try {
            String encodedFilename = UriUtils.encode(filename, StandardCharsets.UTF_8);

            Path path = Paths.get(report.getFilePath() + "/" + filename);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                        .body(resource);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/score")
    public String score(@RequestParam(name = "subject_no", defaultValue = "0") int subject_no,@RequestParam(name = "page", defaultValue = "0") int page, Principal principal, Model model) {
        Professor professor = this.professorService.getProfessor(principal.getName());
        LocalDateTime today = LocalDateTime.now();
        String year = String.valueOf(today.getYear());
        int month = today.getMonthValue();
        String semester = getSemester(month);
        List<Subject> subjectList = this.professorService.getProfessorSubjects(professor, year, semester);
        if (subject_no == 0) {
            Subject _subject = this.professorService.getFirstProfessorSubject(professor, year, semester);
            if (_subject != null) {
                subject_no = _subject.getNo();
            } else {
                boolean isEmpty = true;
                model.addAttribute("isEmpty", isEmpty);
                model.addAttribute("professor", professor);
                return "portal/professor/professor_score";
            }
        }
        Subject subject = this.subjectService.getSubject(subject_no);
        Page<Sugang> sugangList = this.subjectService.getSubjectSugangs(subject, page);
        boolean isEmpty = false;
        if (sugangList.isEmpty()) {
            isEmpty = true;
        }

        int totalPage = sugangList.getTotalPages();
        int block = 5;
        int currentPage = sugangList.getNumber() + 1;

        int[] pageBlock = getPageBlock(totalPage, currentPage, block);
        int startBlock = pageBlock[0];
        int endBlock = pageBlock[1];

        model.addAttribute("subjectList", subjectList);
        model.addAttribute("subjectNo", subject_no);
        model.addAttribute("sugangList", sugangList);
        model.addAttribute("page", page);
        model.addAttribute("startBlock", startBlock);
        model.addAttribute("endBlock", endBlock);
        model.addAttribute("isEmpty", isEmpty);
        return "portal/professor/professor_score";
    }

    @PostMapping("/modify/score")
    @ResponseBody
    public ResponseEntity<String> modifyScore(@RequestBody List<SugangDTO> scores) {
        for (SugangDTO dto : scores) {
            try {
                System.out.println(dto.getNo()+"- 과제1: "+ dto.getAssignment1()+", 과제2: "+dto.getAssignment2()+", 중간: "+dto.getSemi_score()+", 기말: "+ dto.getFinal_score());
                Sugang sugang = this.sugangService.getSugang(dto.getNo());
                sugang.setAssignment1(dto.getAssignment1());
                sugang.setAssignment2(dto.getAssignment2());
                sugang.setSemi_score(dto.getSemi_score());
                sugang.setFinal_score(dto.getFinal_score());
                this.sugangService.updateSugang(sugang);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("저장 실패");
            }
        }
        return ResponseEntity.ok("저장 되었습니다.");
    }

    @GetMapping("/check/pwd")
    public String checkPwd(Principal principal, Model model) {
        Professor professor = this.professorService.getProfessor(principal.getName());
        model.addAttribute("professor", professor);
        return "portal/professor/professor_password_check";
    }

    @PostMapping("/check/pwd")
    @ResponseBody
    public Map<String, String> checkPwd(Principal principal, @RequestParam("password") String password) {
        Professor professor = this.professorService.getProfessor(principal.getName());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Map<String, String> response = new HashMap<>();
        if (professor == null) {
            response.put("message", "일치하는 정보를 찾을수 없습니다.");

        } else if (passwordEncoder.matches(password, professor.getPassword())) {
            response.put("status", "success");
            response.put("message", "일치하는 비밀번호입니다.");
            response.put("redirectUrl", "/p/modify/pwd");

        } else {
            response.put("message", "비밀번호가 일치하지 않습니다.");
        }
        return response;
    }

    @GetMapping("/modify/pwd")
    public String modifyPwd(Principal principal, Model model) {
        Professor professor = this.professorService.getProfessor(principal.getName());
        model.addAttribute("professor", professor);
        return "portal/professor/professor_password_modify";
    }

    @PostMapping("/modify/pwd")
    @ResponseBody
    public Map<String, String> modifyPwd(Principal principal, @RequestParam("password") String password) {
        Map<String, String> response = new HashMap<>();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Professor professor = this.professorService.getProfessor(principal.getName());
        if (professor == null) {
            response.put("message", "일치하는 정보를 찾을수 없습니다.");
        } else {
            professor.setPassword(passwordEncoder.encode(password));
            this.professorService.updatePassword(professor);
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

    private int[] getPageBlock(int totalPages, int currentPage, int block) {
        int startBlock = (((currentPage - 1) / block) * block) + 1;
        int endBlock = startBlock + block - 1;

        if (endBlock > totalPages) {
            endBlock = totalPages;
        }
        return new int[] { startBlock, endBlock };
    }
}
