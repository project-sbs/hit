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

		Major major1 = new Major();
		major1.setName("간호학과");
		this.majorService.insertMajor(major1);

		Major major2 = new Major();
		major2.setName("생명공학과");
		this.majorService.insertMajor(major2);

		Major major3 = new Major();
		major3.setName("경영학과");
		this.majorService.insertMajor(major3);

		Major major4 = new Major();
		major4.setName("실용음악과");
		this.majorService.insertMajor(major4);

	}


	@Test
	void testInsertStudent() {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		Student student = new Student();
		student.setId("20241111");
		student.setName("홍길동");
		student.setEmail("hone@gmail.com");
		student.setBirthday("1990-01-01");
		student.setROLE("학생");

		student.setPassword(bCryptPasswordEncoder.encode("900101"));

		student.setPhone("010-1234-1234");
		student.setMajor(this.majorService.getMajor(1));
		this.studentService.insertStudent(student);

		Student student1 = new Student();
		student1.setId("20242222");
		student1.setName("신짱구");
		student1.setEmail("sin@gmail.com");
		student1.setBirthday("1992-04-13");
		student1.setROLE("학생");

		student1.setPassword(bCryptPasswordEncoder.encode("900202"));

		student1.setPhone("010-1111-1111");
		student1.setMajor(this.majorService.getMajor(2));
		this.studentService.insertStudent(student1);



		Student student2 = new Student();
		student2.setId("20243333");
		student2.setName("김철수");
		student2.setEmail("kim@gmail.com");
		student2.setBirthday("1995-02-11");
		student2.setROLE("학생");

		student2.setPassword(bCryptPasswordEncoder.encode("900303"));

		student2.setPhone("010-2222-2222");
		student2.setMajor(this.majorService.getMajor(3));
		this.studentService.insertStudent(student2);



		Student student3 = new Student();
		student3.setId("20244444");
		student3.setName("둘리");
		student3.setEmail("park@gmail.com");
		student3.setBirthday("1997-05-25");
		student3.setROLE("학생");

		student3.setPassword(bCryptPasswordEncoder.encode("900404"));

		student3.setPhone("010-3333-3333");
		student3.setMajor(this.majorService.getMajor(4));
		this.studentService.insertStudent(student3);



		Student student4 = new Student();
		student4.setId("20245555");
		student4.setName("고길동");
		student4.setEmail("go@gmail.com");
		student4.setBirthday("1991-06-01");
		student4.setROLE("학생");

		student4.setPassword(bCryptPasswordEncoder.encode("900505"));

		student4.setPhone("010-1555-5555");
		student4.setMajor(this.majorService.getMajor(5));
		this.studentService.insertStudent(student4);


		Student student5 = new Student();
		student5.setId("20246666");
		student5.setName("도우너");
		student5.setEmail("dou@gmail.com");
		student5.setBirthday("1999-08-15");
		student5.setROLE("학생");

		student5.setPassword(bCryptPasswordEncoder.encode("900606"));

		student5.setPhone("010-6666-6666");
		student5.setMajor(this.majorService.getMajor(1));
		this.studentService.insertStudent(student5);


		Student student6 = new Student();
		student6.setId("20247777");
		student6.setName("또오치");
		student6.setEmail("dodo@gmail.com");
		student6.setBirthday("1999-01-11");
		student6.setROLE("학생");

		student6.setPassword(bCryptPasswordEncoder.encode("900707"));

		student6.setPhone("010-7777-7777");
		student6.setMajor(this.majorService.getMajor(1));
		this.studentService.insertStudent(student6);


		Student student7 = new Student();
		student7.setId("20248888");
		student7.setName("고양이");
		student7.setEmail("cat@gmail.com");
		student7.setBirthday("2000-04-08");
		student7.setROLE("학생");

		student7.setPassword(bCryptPasswordEncoder.encode("900808"));

		student7.setPhone("010-8888-8888");
		student7.setMajor(this.majorService.getMajor(2));
		this.studentService.insertStudent(student7);


		Student student8 = new Student();
		student8.setId("20249999");
		student8.setName("강아지");
		student8.setEmail("dog@gmail.com");
		student8.setBirthday("2000-11-11");
		student8.setROLE("학생");

		student8.setPassword(bCryptPasswordEncoder.encode("900909"));

		student8.setPhone("010-9999-9999");
		student8.setMajor(this.majorService.getMajor(3));
		this.studentService.insertStudent(student8);


		Student student9 = new Student();
		student9.setId("20241234");
		student9.setName("고라니");
		student9.setEmail("ing@gmail.com");
		student9.setBirthday("1989-12-25");
		student9.setROLE("학생");

		student9.setPassword(bCryptPasswordEncoder.encode("901234"));

		student9.setPhone("010-4321-4321");
		student9.setMajor(this.majorService.getMajor(5));
		this.studentService.insertStudent(student9);
	}

	@Test
	void testInsertProfessor() {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


		Professor professor = new Professor();
		professor.setId("P19600101");
		professor.setName("김철수");
		professor.setEmail("cheol@gmail.com");
		professor.setBirthday("1960-01-01");
		professor.setROLE("교수");

		professor.setPassword(bCryptPasswordEncoder.encode("600101"));

		professor.setPhone("010-1111-1111");
		professor.setMajor(this.majorService.getMajor(1));
		this.professorService.insertProfessor(professor);


		Professor professor1 = new Professor();
		professor1.setId("P19620202");
		professor1.setName("박찬호");
		professor1.setEmail("LA@gmail.com");
		professor1.setBirthday("1962-02-02");
		professor1.setROLE("교수");

		professor1.setPassword(bCryptPasswordEncoder.encode("600202"));

		professor1.setPhone("010-2222-2222");
		professor1.setMajor(this.majorService.getMajor(1));
		this.professorService.insertProfessor(professor1);


		Professor professor2 = new Professor();
		professor2.setId("P19600303");
		professor2.setName("박지성");
		professor2.setEmail("jee@gmail.com");
		professor2.setBirthday("1960-03-03");
		professor2.setROLE("교수");

		professor2.setPassword(bCryptPasswordEncoder.encode("600303"));

		professor2.setPhone("010-3333-3333");
		professor2.setMajor(this.majorService.getMajor(2));
		this.professorService.insertProfessor(professor2);


		Professor professor3 = new Professor();
		professor3.setId("P19600404");
		professor3.setName("오오류");
		professor3.setEmail("notFound@gmail.com");
		professor3.setBirthday("1960-04-11");
		professor3.setROLE("교수");

		professor3.setPassword(bCryptPasswordEncoder.encode("600404"));

		professor3.setPhone("010-4443-4321");
		professor3.setMajor(this.majorService.getMajor(2));
		this.professorService.insertProfessor(professor3);


		Professor professor4 = new Professor();
		professor4.setId("P19600505");
		professor4.setName("손오공");
		professor4.setEmail("son@gmail.com");
		professor4.setBirthday("1960-05-05");
		professor4.setROLE("교수");

		professor4.setPassword(bCryptPasswordEncoder.encode("600505"));

		professor4.setPhone("010-5555-5555");
		professor4.setMajor(this.majorService.getMajor(3));
		this.professorService.insertProfessor(professor4);


		Professor professor5 = new Professor();
		professor5.setId("P19600606");
		professor5.setName("양교수");
		professor5.setEmail("sheep@gmail.com");
		professor5.setBirthday("1960-06-06");
		professor5.setROLE("교수");

		professor5.setPassword(bCryptPasswordEncoder.encode("600606"));

		professor5.setPhone("010-6666-6666");
		professor5.setMajor(this.majorService.getMajor(3));
		this.professorService.insertProfessor(professor5);


		Professor professor6 = new Professor();
		professor6.setId("P19600707");
		professor6.setName("김영어");
		professor6.setEmail("English@gmail.com");
		professor6.setBirthday("1960-07-07");
		professor6.setROLE("교수");

		professor6.setPassword(bCryptPasswordEncoder.encode("600707"));

		professor6.setPhone("010-7777-7777");
		professor6.setMajor(this.majorService.getMajor(4));
		this.professorService.insertProfessor(professor6);


		Professor professor7 = new Professor();
		professor7.setId("P19600808");
		professor7.setName("팔공팔");
		professor7.setEmail("eight@gmail.com");
		professor7.setBirthday("1960-08-08");
		professor7.setROLE("교수");

		professor7.setPassword(bCryptPasswordEncoder.encode("600808"));

		professor7.setPhone("010-8888-8888");
		professor7.setMajor(this.majorService.getMajor(4));
		this.professorService.insertProfessor(professor7);


		Professor professor8 = new Professor();
		professor8.setId("P19600909");
		professor8.setName("비둘기");
		professor8.setEmail("bee@gmail.com");
		professor8.setBirthday("1960-09-09");
		professor8.setROLE("교수");

		professor8.setPassword(bCryptPasswordEncoder.encode("600909"));

		professor8.setPhone("010-9999-9999");
		professor8.setMajor(this.majorService.getMajor(5));
		this.professorService.insertProfessor(professor8);

		Professor professor9 = new Professor();
		professor9.setId("P19601010");
		professor9.setName("열번째");
		professor9.setEmail("ten@gmail.com");
		professor9.setBirthday("1960-10-10");
		professor9.setROLE("교수");

		professor9.setPassword(bCryptPasswordEncoder.encode("601010"));

		professor9.setPhone("010-1010-1010");
		professor9.setMajor(this.majorService.getMajor(5));
		this.professorService.insertProfessor(professor9);
	}
}
