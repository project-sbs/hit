package com.project.hit;

import com.project.hit.admin.Admin;
import com.project.hit.admin.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class HitApplicationTests {

	@Autowired
	private AdminService adminService;

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

}
