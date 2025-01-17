package com.project.hit.admin;

import com.project.hit.DataNotFoundException;
import com.project.hit.board.Board;
import com.project.hit.board.BoardService;
import com.project.hit.major.Major;
import com.project.hit.major.MajorService;
import com.project.hit.professor.Professor;
import com.project.hit.professor.ProfessorDTO;
import com.project.hit.professor.ProfessorService;
import com.project.hit.student.Student;
import com.project.hit.student.StudentService;
import com.project.hit.subject.Subject;
import com.project.hit.subject.SubjectService;
import com.project.hit.user.ProfessorInsertForm;
import com.project.hit.user.StudentInsertForm;
import com.project.hit.user.SubjectInsertForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/a")
public class AdminController {

    private final AdminService adminService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final MajorService majorService;
    private final BoardService boardService;
    private final SubjectService subjectService;

    @Value("src/main/resources/static/profile/")
    private String profileDir;

    private void createProfileDir() {
        File dir = new File(profileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @GetMapping("/home")
    public String home(Principal principal, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "3") int size){

        int totalSchedulers = this.boardService.getTotalSchedulersCount();
        int totalPages = (int) Math.ceil((double) totalSchedulers / size);

        if (page >= totalPages) {
            page = totalPages - 1;
        }
        if (page < 0) {
            page = 0;
        }

        LocalDate currentDate = LocalDate.now();
        Admin admin = this.adminService.getAdmin(principal.getName());
        List<Board> notices = this.boardService.getTop6Boards("notice");
        List<Board> educations = this.boardService.getTop6Boards("edu");
        List<Board> freebulletins = this.boardService.getTop6Boards("free");
        List<Board> jobpostings = this.boardService.getTop6Boards("hire");
        List<Board> contents = this.boardService.getTop6Boards("con");
        List<Board> schedulers = this.boardService.getSchedulersByPage(page, size);

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("notices", notices);
        model.addAttribute("educations", educations);
        model.addAttribute("freebulletins", freebulletins);
        model.addAttribute("jobpostings", jobpostings);
        model.addAttribute("contents", contents);
        model.addAttribute("schedulers", schedulers);
        model.addAttribute("admin", admin);
        model.addAttribute("page", page);

        return "portal/admin/admin_home";
    }

    @GetMapping("/class")
    public String classManage(SubjectInsertForm subjectInsertForm, Principal principal, Model model,
                              @RequestParam(value = "majorPage", defaultValue = "0") int majorPage,
                              @RequestParam(value = "generalPage", defaultValue = "0") int generalPage,
                              @RequestParam(value = "major", defaultValue = "전체") String major,
                              @RequestParam(value = "department", defaultValue = "0") int department) {

        Admin admin = this.adminService.getAdmin(principal.getName());
        List<Major> majorList = this.majorService.getAllMajors();
        List<Professor> professorList = this.professorService.getAllProfessors();
        if(major.equals("전체")) {
            Page<Subject> subjectPage1 = this.subjectService.getSubjectList("전공", majorPage);
            Page<Subject> subjectPage2 = this.subjectService.getSubjectList("교양", generalPage);

            int[] majorBlock = getPageBlock(subjectPage1);
            int[] generalBlock = getPageBlock(subjectPage2);

            model.addAttribute("sub_startBlock", majorBlock[0]);
            model.addAttribute("sub2_sub_startBlock", generalBlock[0]);
            model.addAttribute("sub_endBlock", majorBlock[1]);
            model.addAttribute("sub2_endBlock", generalBlock[1]);
            model.addAttribute("subjectPage1", subjectPage1);
            model.addAttribute("subjectPage2", subjectPage2);
        } else if (major.equals("전공")) {
            Page<Subject> subjectPage1 = this.subjectService.getSubjectList("전공", majorPage, department);
            int[] majorBlock = getPageBlock(subjectPage1);

            model.addAttribute("sub_startBlock", majorBlock[0]);
            model.addAttribute("sub2_sub_startBlock", majorBlock[1]);
            model.addAttribute("subjectPage1", subjectPage1);
        } else {
            Page<Subject> subjectPage2 = this.subjectService.getSubjectList("교양", generalPage, department);
            int[] generalBlock = getPageBlock(subjectPage2);

            model.addAttribute("sub_startBlock", generalBlock[0]);
            model.addAttribute("sub2_sub_startBlock", generalBlock[1]);
            model.addAttribute("subjectPage2", subjectPage2);
        }

        model.addAttribute("professorList", professorList);
        model.addAttribute("majorList", majorList);
        model.addAttribute("admin", admin);
        model.addAttribute("major", major);
        model.addAttribute("department", department);
        model.addAttribute("majorPage", majorPage);
        model.addAttribute("generalPage", generalPage);

        return "portal/admin/admin_classManage";
    }

    @GetMapping("/major")
    public String majorManage(Principal principal, Model model) {
        List<Major> majorList = this.majorService.getAllMajors();
        Admin admin = this.adminService.getAdmin(principal.getName());

        model.addAttribute("admin", admin);
        model.addAttribute("majorList", majorList);
        return "portal/admin/admin_majorManage";
    }

    @GetMapping("/person")
    public String personManage(StudentInsertForm studentInsertForm, ProfessorInsertForm professorInsertForm, Principal principal, Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "keyword", defaultValue = "") String keyword,
                               @RequestParam(name = "field", defaultValue = "") String field, @RequestParam(name = "major", defaultValue = "-1") int major_id,
                               @RequestParam(name = "person", defaultValue = "학부생") String person){
        List<Major> majorList = this.majorService.getAllMajors();
        Page<Student> studentPaging = this.studentService.getStudents(field, keyword, page, major_id);
        Page<Professor> professorPaging = this.professorService.getProfessors(field, keyword, page, major_id);
        Admin admin = this.adminService.getAdmin(principal.getName());

        int[] stu_block = getPageBlock(studentPaging);
        int[] prof_block = getPageBlock(professorPaging);

        int stu_startBlock = stu_block[0];
        int stu_endBlock = stu_block[1];

        int pro_startBlock = prof_block[0];
        int pro_endBlock = prof_block[1];

        model.addAttribute("stu_startBlock", stu_startBlock);
        model.addAttribute("stu_endBlock", stu_endBlock);
        model.addAttribute("pro_startBlock", pro_startBlock);
        model.addAttribute("pro_endBlock", pro_endBlock);
        model.addAttribute("keyword", keyword);
        model.addAttribute("field", field);
        model.addAttribute("page", page);
        model.addAttribute("major", major_id);
        model.addAttribute("person", person);

        model.addAttribute("admin", admin);
        model.addAttribute("majorList", majorList);
        model.addAttribute("studentPaging", studentPaging);
        model.addAttribute("professorPaging", professorPaging);

        return "portal/admin/admin_personManage";
    }

    @GetMapping("/board")
    public String notice(Principal principal, Model model) {
        Admin admin = this.adminService.getAdmin(principal.getName());
        model.addAttribute("admin", admin);
        return "portal/admin/admin_board";
    }

    @GetMapping("/subject/delete/{no}")
    @PreAuthorize("hasRole('ROLE_관리자')")
    public String deleteSubjects(@PathVariable("no") Integer no) {
        Subject subject = subjectService.getSubjectById(no);
        if (subject != null) {
            subjectService.deleteSubjects(subject);
        } else {
            throw new DataNotFoundException("Subject not found for id: " + no);
        }
        return "redirect:/a/class";
    }

    @PostMapping("/insert/board")
    public String insertBoard(@RequestParam("type") String type, @RequestParam("title") String title,
                              @RequestParam("content") String content, @RequestParam("date") String date) {
        Board board = new Board();
        board.setContent(content);
        board.setReg_date(LocalDateTime.now());
        board.setTitle(title);
        board.setType(type);
        board.setDate(date);
        if(!type.equals("scheduler")){
            board.setContent(content);
        } else {
            board.setDate(date);
        }

        this.boardService.insertBoard(board);

        return "redirect:/a/board";
    }

    @GetMapping("/insert/subject")
    public String subjectInsert(@Valid SubjectInsertForm subjectInsertForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "portal/admin/admin_classManage";
        }

        try {
            Subject subject = insertSubject(subjectInsertForm);
            this.subjectService.addSubject(subject);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", "이미 등록된 사용자입니다.");
            return "portal/admin/admin_classManage";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", e.getMessage());
            return "portal/admin/admin_classManage";
        }

        return "redirect:/a/class";
    }

    @PostMapping("/insert/student")
    public String studentInsert(@Valid StudentInsertForm studentInsertForm, BindingResult bindingResult, @RequestParam("photo") MultipartFile photo) {

        if (bindingResult.hasErrors()) {
            return "portal/admin/admin_personManage";
        }

        try {
            Student student = insertStudent(studentInsertForm);
            student.setProfile(addPhoto(photo, student.getId()));
            this.studentService.addStudent(student);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", "이미 등록된 사용자입니다.");
            return "portal/admin/admin_personManage";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", e.getMessage());
            return "portal/admin/admin_personManage";
        }

        return "redirect:/a/person";
    }

    @PostMapping("/insert/professor")
    public String professorInsert(@Valid ProfessorInsertForm professorInsertForm, BindingResult bindingResult, @RequestParam("photo") MultipartFile photo) {

        if (bindingResult.hasErrors()) {
            return "portal/admin/admin_personManage";
        }

        try {
            Professor professor = insertProfessor(professorInsertForm);
            professor.setProfile(addPhoto(photo, professor.getId()));
            this.professorService.addProfessor(professor);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", "이미 등록된 사용자입니다.");
            return "portal/admin/admin_personManage";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("insertFailed", e.getMessage());
            return "portal/admin/admin_personManage";
        }

        return "redirect:/a/person";
    }

    @PostMapping("/insert/major")
    public String majorInsert(@RequestParam("name") String name, @RequestParam("capacity") int capacity) {
        Major major = new Major();
        major.setName(name);
        major.setTotalStudent(capacity);
        major.setChair("");

        this.majorService.insertMajor(major);
        return "redirect:/a/major";
    }

    @PostMapping("/modify/student")
    public String modifyStudent(@RequestParam("major") int major, @RequestParam("studentId") String id, @RequestParam("name") String name, @RequestParam("dob") String dob,
                                @RequestParam("phone") String phone, @RequestParam("email") String email, @RequestParam("credits") String credits,
                                @RequestParam("status") String status) {
        Student student = new Student();
        Major mjr = this.majorService.getMajor(major);
        student.setMajor(mjr);
        student.setId(id);
        student.setName(name);
        student.setBirthday(dob);
        student.setEmail(email);
        student.setPhone(phone);
        student.setStatus(status);

        this.studentService.updateStudent(student);

        return "redirect:/a/person";
    }

    @PostMapping("/modify/professor")
    public String modifyProfessor(@RequestParam("major") int major, @RequestParam("professorId") String id, @RequestParam("name") String name, @RequestParam("dob") String dob,
                                  @RequestParam("phone") String phone, @RequestParam("email") String email, @RequestParam("position") String position, Principal principal) {
        Professor professor = new Professor();
        Major mjr = this.majorService.getMajor(major);
        professor.setMajor(mjr);
        professor.setId(id);
        professor.setName(name);
        professor.setBirthday(dob);
        professor.setPhone(phone);
        professor.setEmail(email);
        professor.setROLE(position);

        this.professorService.updateProfessor(professor);
        return "redirect:/a/person";
    }

    @GetMapping("/update/professorList")
    public ResponseEntity<List<ProfessorDTO>> updateProfessorList(@RequestParam("departmentId") Integer departmentId) {
        if (departmentId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        Major major = this.majorService.getMajor(departmentId);
        List<ProfessorDTO> professorList = this.professorService.getProfessorsByMajor(major);

        return ResponseEntity.ok(professorList);
    }

    @GetMapping("/update/professorAllList")
    public ResponseEntity<List<ProfessorDTO>> updateProfessorAllList() {
        List<ProfessorDTO> professorDTOList = this.professorService.getProfessorAllList();

        return ResponseEntity.ok(professorDTOList);
    }

    @GetMapping("/student/count")
    @ResponseBody
    public long[] getStudentCount() {
        return this.adminService.getStduentCnt();
    }

    private Subject insertSubject(SubjectInsertForm subjectInsertForm){
        Subject subject = new Subject();

        subject.setYear(subjectInsertForm.getYear());
        subject.setSemester(subjectInsertForm.getSemester());
        subject.setType(subjectInsertForm.getType());
        subject.setName(subjectInsertForm.getName());
        String time = subjectInsertForm.getStartTime() + "-" + subjectInsertForm.getEndTime();
        subject.setTime(time);
        subject.setWeek(subjectInsertForm.getWeek());
        subject.setCredits(subjectInsertForm.getCredits());
        subject.setMaxPersonnel(subjectInsertForm.getMaxPersonnel());

        // 전공
        if (subject.getType().equals("전공")) {
            int majorCode = Integer.parseInt(subjectInsertForm.getMajor());
            Major major = majorService.getMajor(majorCode);
            subject.setMajor(major);
        }

        // 교수
        Long professorNo = subjectInsertForm.getProfessorNo();
        Professor professor = professorService.getProfessor(professorNo);
        subject.setProfessor(professor);

        return subject;
    }

    private Student insertStudent(StudentInsertForm studentInsertForm) {
        Student student = new Student();
        student.setId(studentInsertForm.getId());

        String birthday = studentInsertForm.getBirthday();
        student.setBirthday(birthday);

        String[] split = birthday.split("-"); // '1998-01-01' '1998' '01' '01'
        String password = split[0].substring(2) + split[1] + split[2]; // '980101'
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        student.setPassword(passwordEncoder.encode(password));

        student.setName(studentInsertForm.getName());
        student.setEmail(studentInsertForm.getEmail());
        student.setPhone(studentInsertForm.getPhone());
        int majorCode = Integer.parseInt(studentInsertForm.getMajor());
        Major major = majorService.getMajor(majorCode);
        student.setMajor(major);
        student.setROLE("학생");

        return student;
    }

    private Professor insertProfessor(ProfessorInsertForm professorInsertForm) {
        Professor professor = new Professor();
        professor.setId(professorInsertForm.getId());

        String birthday = professorInsertForm.getBirthday();
        professor.setBirthday(birthday);

        String[] split = birthday.split("-");
        String password = split[0].substring(2) + split[1] + split[2];
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        professor.setPassword(passwordEncoder.encode(password));

        professor.setName(professorInsertForm.getName());
        professor.setEmail(professorInsertForm.getEmail());
        professor.setPhone(professorInsertForm.getPhone());
        int majorCode = Integer.parseInt(professorInsertForm.getMajor());
        Major major = majorService.getMajor(majorCode);
        professor.setMajor(major);
        professor.setROLE(professorInsertForm.getRole());  // 정교수 등등

        return professor;
    }

    private String addPhoto(MultipartFile file, String id) {
        String pathDir = null;
        if (!file.isEmpty()) {
            try {
                createProfileDir();
                File path = new File(profileDir + id);
                System.out.println("저장 경로 : " + path.getAbsolutePath());
                if (!path.exists()) {
                    path.mkdirs();
                }
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(path.getAbsolutePath(), fileName);
                Files.write(filePath, file.getBytes());
                pathDir = "/profile/" + id + "/" + fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pathDir;
    }

    private int[] getPageBlock(Page<?> pageList) {
        int totalPages = pageList.getTotalPages();
        int block = 5;
        int currentPage = pageList.getNumber() + 1;

        int startBlock = (((currentPage - 1) / block) * block) + 1;
        int endBlock = startBlock + block - 1;

        if (endBlock > totalPages) {
            endBlock = totalPages;
        }
        return new int[] { startBlock, endBlock };
    }

}
