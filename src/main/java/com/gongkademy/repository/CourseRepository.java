package com.gongkademy.repository;

import com.gongkademy.domain.course.Course;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Optional<Course> findById(Long courseId);
    List<Course> findCourse();
}
