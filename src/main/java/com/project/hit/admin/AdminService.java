package com.project.hit.admin;

import com.project.hit.DataNotFoundException;
import com.project.hit.student.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void insertAdmin(Admin admin) {
        this.adminRepository.save(admin);
    }

    public Admin getAdmin(String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isPresent()) {
            return admin.get();
        } else {
            throw new DataNotFoundException("Admin not found id : " + id);
        }
    }

    public long[] getStduentCnt() {
        long[] studentCnt = new long[3];

        studentCnt[0] = this.studentRepository.countByStudent();
        studentCnt[1] = this.studentRepository.countBySpecificStatus("재학");
        studentCnt[2] = this.studentRepository.countBySpecificStatus("휴학");

        return studentCnt;
    }
}
