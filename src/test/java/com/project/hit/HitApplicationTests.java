package com.project.hit;

import com.project.hit.admin.Admin;
import com.project.hit.admin.AdminService;
import com.project.hit.major.Major;
import com.project.hit.major.MajorService;
import com.project.hit.professor.Professor;
import com.project.hit.professor.ProfessorService;
import com.project.hit.student.Student;
import com.project.hit.student.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class HitApplicationTests {

	@Autowired
	private AdminService adminService;
	@Autowired
	private MajorService majorService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ProfessorService professorService;

	@Test
	void contextLoads() {
	}

	@Test
	void testInsertAdmin() {
		Admin admin = new Admin();
		admin.setId("admin");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		admin.setPassword(bCryptPasswordEncoder.encode("1234"));
		admin.setName("관리자");
		admin.setEmail("admin@gmail.com");
		admin.setROLE("관리자");
		admin.setPhone("010-1234-1234");
		this.adminService.insertAdmin(admin);
	}

	@Test
	void testInsertMajor() {
		Major major = new Major();
		major.setName("컴퓨터공학과");
		this.majorService.insertMajor(major);
	}

	@Test
	void testInsertStudent() {
		Student student = new Student();
		student.setId("20241111");
		student.setName("홍길동");
		student.setEmail("hone@gmail.com");
		student.setBirthday("1990-01-01");
		student.setROLE("학생");

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		student.setPassword(bCryptPasswordEncoder.encode("900101"));

		student.setPhone("010-1234-1234");
		student.setMajor(this.majorService.getMajor(1));
		this.studentService.insertStudent(student);
	}

	@Test
	void testInsertProfessor() {
		Professor professor = new Professor();
		professor.setId("P19600101");
		professor.setName("김철수");
		professor.setEmail("cheol@gmail.com");
		professor.setBirthday("1960-01-01");
		professor.setROLE("교수");

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		professor.setPassword(bCryptPasswordEncoder.encode("600101"));

		professor.setPhone("010-1111-1111");
		professor.setMajor(this.majorService.getMajor(1));
		this.professorService.insertProfessor(professor);
	}
}
