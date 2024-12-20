package com.gongkademy.repository;

import com.gongkademy.domain.Course;

public interface CourseRepository {
    Course findById(Long courseId);
}
