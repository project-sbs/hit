package com.project.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("/")
    public String main() {return "main";}

    @GetMapping("/check/account")
    public String checkAccount() {return "check_account";}

    @GetMapping("/about/department")
    public String istrative() {return "aboutAdministrative";}

    @GetMapping("/about/hit")
    public String aboutHit() {return "aboutHit";}

    @GetMapping("/about/notice")
    public String notice() {return "aboutNotice";}

    @GetMapping("/about/us")
    public String us() {return "aboutUs";}

    @GetMapping("/apply")
    public String apply() {return "howToApply";}

    @GetMapping("/research/archive")
    public String researchArchive() {return "researchArchive";}

    @GetMapping("/research/overview")
    public String researchOverview() {return "researchOverview";}

    @GetMapping("/study")
    public String study() {return "studyInKorea";}
}
