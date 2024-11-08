package com.project.hit.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/s")
public class StudentController {

    @GetMapping("/home")
    public String home() {return "portal/student/student_home";}

    @GetMapping("/info")   // 학생
    public String info() {return "portal/student/student_info";}

    @GetMapping("/score")
    public String score() {return "portal/student/student_score";}

    @GetMapping("/report")
    public String report() {return "portal/student/student_report";}
}
