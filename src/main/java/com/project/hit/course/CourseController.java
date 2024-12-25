package com.project.hit.course;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/a/course/status")
    public String status() {
        Course course = this.courseService.getStatus();
        return course.getStatus();
    }

    @GetMapping("/a/course/open")
    public String openServer() {
        try {
            this.courseService.updateStatus("OPEN");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("/a/course/close")
    public String closeServer() {
        try {
            this.courseService.updateStatus("CLOSE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("/s/course/status")
    public String courseStatus() {
        Course course = this.courseService.getStatus();
        return course.getStatus();
    }
}
