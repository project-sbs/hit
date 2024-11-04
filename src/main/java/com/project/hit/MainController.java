package com.project.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("/")
    public String main() {return "main";}

    @GetMapping("/portal")
    public String portal_main() {return "portal/portal_main";}
}
