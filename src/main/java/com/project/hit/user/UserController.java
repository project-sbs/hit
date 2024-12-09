package com.project.hit.user;

import com.project.hit.student.Student;
import com.project.hit.student.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final StudentService studentService;
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/login")
    public String login() {return "login";}

    @PostMapping("/api/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> data) {
        String id = data.get("id");
        String email = data.get("email");
        boolean isMatchInfo = this.studentService.isMatchedIdAndEmail(id, email);
        httpSession.setAttribute("userId", id);

        if (!isMatchInfo) {   // id, email 유효성 검사
            return ResponseEntity.badRequest().body("일치하는 정보를 찾을 수 없습니다.");
        }

        return this.userService.sendMail(email);
    }

    @PostMapping("/api/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> data) {
        String email = data.get("emailValue");
        String code = data.get("codeValue");
        System.out.println(email+" / "+code);
        boolean isSuccess = this.userService.checkCode(email, code);

        if (isSuccess) {
            this.userService.deleteAuthcode(email);
            return ResponseEntity.ok("인증에 성공했습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증코드가 일치하지 않습니다.");
        }
    }

    @GetMapping("/update/user")
    public String updateUser(Model model) {
        String userId = (String) httpSession.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            return "redirect:/login";
        }
        httpSession.removeAttribute("userId");
        Student student = studentService.getStudentById(userId);

        model.addAttribute("student", student);
        return "update_user";
    }

    /*@PostMapping("/update/password")
    public String updatePassword(@RequestParam("id") String id, @RequestParam("password") String password) {
        Student student = studentService.getStudentById(id);

    }*/
}
