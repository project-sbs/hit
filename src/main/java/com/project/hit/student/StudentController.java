package com.project.hit.student;

import com.project.hit.DataNotFoundException;
import com.project.hit.admin.Admin;
import com.project.hit.admin.AdminService;
import com.project.hit.board.Board;
import com.project.hit.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/s")
public class StudentController {

    private final StudentService studentService;
    private final BoardService boardService;
    private AdminService adminService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        List<Board> notices = this.boardService.getTop6Boards("notice");
        List<Board> educations = this.boardService.getTop6Boards("edu");
        List<Board> freebulletins = this.boardService.getTop6Boards("free");
        List<Board> jobpostings = this.boardService.getTop6Boards("hire");

        model.addAttribute("jobpostings", jobpostings);
        model.addAttribute("freebulletins", freebulletins);
        model.addAttribute("notices", notices);
        model.addAttribute("educations", educations);
        model.addAttribute("student", student);
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
    public String course(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());

        model.addAttribute("student", student);
        return "portal/student/student_courseChoice";
    }

    @GetMapping("/board")
    public String board(Model model, Principal principal) {
        Student student = this.studentService.getStudentById(principal.getName());
        List<Board> notices = this.boardService.getTop6Boards("notice");
        List<Board> educations = this.boardService.getTop6Boards("edu");
        List<Board> freebulletins = this.boardService.getTop6Boards("free");
        List<Board> jobpostings = this.boardService.getTop6Boards("hire");
        List<Board> contents = this.boardService.getTop6Boards("con");

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
}

