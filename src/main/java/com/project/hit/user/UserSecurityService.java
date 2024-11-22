package com.project.hit.user;

import com.project.hit.admin.Admin;
import com.project.hit.admin.AdminRepository;
import com.project.hit.professor.Professor;
import com.project.hit.professor.ProfessorRepository;
import com.project.hit.student.Student;
import com.project.hit.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        if (id.contains("admin")) {
            Optional<Admin> _user = this.adminRepository.findById(id);

            if (_user.isEmpty()) {
                throw new UsernameNotFoundException("일치하는 아이디를 찾을수 없습니다.");
            }
            Admin admin = _user.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(admin.getROLE().equals("관리자")) {
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            } else {
                throw new UsernameNotFoundException("권한이 일치 하지 않습니다.");
            }

            return new User(admin.getId(), admin.getPassword(), authorities);

        } else if (id.charAt(0) == 'P') {
            Optional<Professor> _user = professorRepository.findById(id);

            if (_user.isEmpty()) {
                throw new UsernameNotFoundException("일치하는 아이디를 찾을수 없습니다.");
            }
            Professor professor = _user.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(professor.getROLE().equals("학과장") || professor.getROLE().equals("교수") || professor.getROLE().equals("부교수") || professor.getROLE().equals("시간강사")) {
                authorities.add(new SimpleGrantedAuthority(UserRole.PROFESSOR.getValue()));
            } else {
                throw new UsernameNotFoundException("권한이 일치 하지 않습니다.");
            }

            return new User(professor.getId(), professor.getPassword(), authorities);
        } else {
            Optional<Student> _user = this.studentRepository.findById(id);

            if (_user.isEmpty()) {
                throw new UsernameNotFoundException("일치하는 아이디를 찾을수 없습니다.");
            }
            Student student = _user.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if(student.getROLE().equals("학생")) {
                authorities.add(new SimpleGrantedAuthority(UserRole.STUDENT.getValue()));
            } else {
                throw new UsernameNotFoundException("권한이 일치 하지 않습니다.");
            }

            return new User(student.getId(), student.getPassword(), authorities);
        }

    }
}
