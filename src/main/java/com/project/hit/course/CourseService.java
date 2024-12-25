package com.project.hit.course;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course getStatus() {
        return this.courseRepository.findById(1);
    }

    @Transactional
    public void updateStatus(String status) {
        Course course = this.courseRepository.findById(1);
        course.setStatus(status);
        this.courseRepository.save(course);
    }

    @Transactional
    public void addCourse(Course course) {
        this.courseRepository.save(course);
    }
}
