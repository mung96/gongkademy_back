package com.gongkademy.repository;

import com.gongkademy.domain.course.Course;
import java.util.Optional;

public interface CourseRepository {
    Optional<Course> findById(Long courseId);
}
