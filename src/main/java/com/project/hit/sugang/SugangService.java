package com.project.hit.sugang;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SugangService {
    private final SugangRepository sugangRepository;

    public List<Sugang> getCurrentSugangs(String semester, String year) {
        return this.sugangRepository.findCurrentSubjectList(semester, year);
    }
}
