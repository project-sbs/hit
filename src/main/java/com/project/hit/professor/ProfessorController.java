package com.project.hit.professor;

import com.project.hit.subject.Subject;
import com.project.hit.subject.SubjectService;
import com.project.hit.sugang.Sugang;
import com.project.hit.sugang.SugangDTO;
import com.project.hit.sugang.SugangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/p")
public class ProfessorController {

    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final SugangService sugangService;

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        Professor professor = this.professorService.getProfessor(principal.getName());

        model.addAttribute("professor", professor);
        return "portal/professor/professor_home";
    }

    @GetMapping("/info")
    public String info() {return "portal/professor/professor_info";}

    @GetMapping("/report")
    public String report() {return "portal/professor/professor_report_check";}

    @GetMapping("/score")
    public String score(@RequestParam(name = "subject_no", defaultValue = "0") int subject_no,@RequestParam(name = "page", defaultValue = "0") int page,Principal principal, Model model) {
        Professor professor = this.professorService.getProfessor(principal.getName());
        List<Subject> subjectList = this.professorService.getProfessorSubjects(professor);
        if (subject_no == 0) {
            subject_no = this.professorService.getFirstProfessorSubject(professor).getNo();
        }
        Subject subject = this.subjectService.getSubject(subject_no);
        Page<Sugang> sugangList = this.subjectService.getSubjectSugangs(subject, page);

        int totalPage = sugangList.getTotalPages();
        int block = 5;
        int currentPage = sugangList.getNumber() + 1;

        int[] pageBlock = getPageBlock(totalPage, currentPage, block);
        int startBlock = pageBlock[0];
        int endBlock = pageBlock[1];

        model.addAttribute("subjectList", subjectList);
        model.addAttribute("subjectNo", subject_no);
        model.addAttribute("sugangList", sugangList);
        model.addAttribute("page", page);
        model.addAttribute("startBlock", startBlock);
        model.addAttribute("endBlock", endBlock);
        return "portal/professor/professor_score";
    }

    @PostMapping("/modify/score")
    @ResponseBody
    public ResponseEntity<String> modifyScore(@RequestBody List<SugangDTO> scores) {
        for (SugangDTO dto : scores) {
            try {
                System.out.println(dto.getNo()+"- 과제1: "+ dto.getAssignment1()+", 과제2: "+dto.getAssignment2()+", 중간: "+dto.getSemi_score()+", 기말: "+ dto.getFinal_score());
                Sugang sugang = this.sugangService.getSugang(dto.getNo());
                sugang.setAssignment1(dto.getAssignment1());
                sugang.setAssignment2(dto.getAssignment2());
                sugang.setSemi_score(dto.getSemi_score());
                sugang.setFinal_score(dto.getFinal_score());
                this.sugangService.updateSugang(sugang);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("저장 실패");
            }
        }
        return ResponseEntity.ok("저장 되었습니다.");
    }

    private int[] getPageBlock(int totalPages, int currentPage, int block) {
        int startBlock = (((currentPage - 1) / block) * block) + 1;
        int endBlock = startBlock + block - 1;

        if (endBlock > totalPages) {
            endBlock = totalPages;
        }
        return new int[] { startBlock, endBlock };
    }
}
