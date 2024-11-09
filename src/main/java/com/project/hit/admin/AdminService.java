package com.project.hit.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public void insertAdmin(Admin admin) {this.adminRepository.save(admin);}


}
