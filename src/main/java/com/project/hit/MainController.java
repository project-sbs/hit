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
    public String checkAccount() {
        return "check_account";
    }

}
