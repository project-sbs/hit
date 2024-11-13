package com.project.hit.major;

import com.project.hit.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorService {
    private final MajorRepository majorRepository;

    public Major getMajor(int majorCode) {
        Optional<Major> major = this.majorRepository.findById(majorCode);
        if (major.isPresent()) {
            return major.get();
        } else {
            throw new DataNotFoundException("Not found for code : " + majorCode);
        }
    }

    public void insertMajor(Major major) {this.majorRepository.save(major);}

    public List<Major> getAllMajors() {return this.majorRepository.findAll();}

}
