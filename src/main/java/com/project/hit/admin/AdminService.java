package com.project.hit.admin;

import com.project.hit.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public void insertAdmin(Admin admin) {this.adminRepository.save(admin);}

    public Admin getAdmin(String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if(admin.isPresent()) {
            return admin.get();
        } else {
            throw new DataNotFoundException("Admin not found id : " + id);
        }
    }
}
