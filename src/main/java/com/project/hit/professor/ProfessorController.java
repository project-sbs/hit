package com.project.hit.professor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/p")
public class ProfessorController {

    @GetMapping("/home")
    public String home() {return "portal/professor/professor_home";}

    @GetMapping("/info")
    public String info() {return "portal/professor/professor_info";}

    @GetMapping("/report")
    public String report() {return "portal/professor/professor_report_check";}

    @GetMapping("/score")
    public String score() {return "portal/professor/professor_score";}

}
