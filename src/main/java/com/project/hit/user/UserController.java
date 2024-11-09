package com.project.hit.user;

import com.project.hit.major.Major;
import com.project.hit.major.MajorService;
import com.project.hit.professor.Professor;
import com.project.hit.professor.ProfessorService;
import com.project.hit.student.Student;
import com.project.hit.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final StudentService studentService;
    private final ProfessorService professorService;
    private final MajorService majorService = null;

    @GetMapping("/login")
    public String login() {return "login";}

    @GetMapping("/insert")
    public String userInsert(UserInsertForm userInsertForm) {return "portal/admin/admin_personManage";}

    @PostMapping("/insert")
    public String userInsert(@Valid UserInsertForm userInsertForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "portal/admin/admin_personManage";
        }

        try {
            if (userInsertForm.getType().equals("학생")) {
                Student student = insertStudent(userInsertForm);
                this.studentService.addStudent(student);
            } else {
                Professor professor = insertProfessor(userInsertForm);
                this.professorService.addProfessor(professor);
            }
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", "이미 등록된 사용자입니다.");
            return "portal/admin/admin_personManage";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", e.getMessage());
            return "portal/admin/admin_personManage";
        }

        return "portal/admin/admin_personManage";
    }

    private Student insertStudent(UserInsertForm userInsertForm) {
        Student student = new Student();
        student.setId(userInsertForm.getId());

        String birthday = userInsertForm.getBirthday();
        student.setBirthday(birthday);

        String[] split = birthday.split("-");
        String password = split[0].substring(2) + split[1] + split[2];
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        student.setPassword(passwordEncoder.encode(password));

        student.setName(userInsertForm.getUsername());
        student.setEmail(userInsertForm.getEmail());
        student.setPhone(userInsertForm.getPhone());
        Major major = majorService.getMajor(userInsertForm.getMajor());
        student.setMajor(major);
        student.setROLE("학생");

        return student;
    }

    private Professor insertProfessor(UserInsertForm userInsertForm) {
        Professor professor = new Professor();
        professor.setId(userInsertForm.getId());

        String birthday = userInsertForm.getBirthday();
        professor.setBirthday(birthday);

        String[] split = birthday.split("-");
        String password = split[0].substring(2) + split[1] + split[2];
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        professor.setPassword(passwordEncoder.encode(password));

        professor.setName(userInsertForm.getUsername());
        professor.setEmail(userInsertForm.getEmail());
        professor.setPhone(userInsertForm.getPhone());
        Major major = majorService.getMajor(userInsertForm.getMajor());
        professor.setMajor(major);
        professor.setROLE(userInsertForm.getOption());  // 정교수 등등

        return professor;
    }



}
