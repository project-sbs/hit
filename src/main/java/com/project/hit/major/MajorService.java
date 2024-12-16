package com.project.hit.major;

import com.project.hit.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public void insertMajor(Major major) {this.majorRepository.save(major);}

    public List<Major> getAllMajors() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return this.majorRepository.findAll(sort);
    }

    @Transactional
    public void updateChairMan(Major major, String name) {
        Optional<Major> _major = this.majorRepository.findById(major.getId());
        if (_major.isPresent()) {
            Major mjr = _major.get();
            mjr.setChair(name);
            this.majorRepository.save(mjr);
        } else {
            throw new DataNotFoundException("Not found for code : " + major.getId());
        }
    }

    @Transactional
    public void updateChairMan(Major major) {
        Optional<Major> _major = this.majorRepository.findById(major.getId());
        if (_major.isPresent()) {
            Major mjr = _major.get();
            mjr.setChair("");
            this.majorRepository.save(mjr);
        } else {
            throw new DataNotFoundException("Not found for code : " + major.getId());
        }
    }
}
