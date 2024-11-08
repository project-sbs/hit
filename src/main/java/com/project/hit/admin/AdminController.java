package com.project.hit.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/a")
public class AdminController {

    @GetMapping("/home")
    public String home() {return "portal/admin/admin_home";}

    @GetMapping("/class")
    public String classManage() {return "portal/admin/admin_classManage";}

    @GetMapping("/person")
    public String personManage() {return "portal/admin/admin_personManage";}
}
