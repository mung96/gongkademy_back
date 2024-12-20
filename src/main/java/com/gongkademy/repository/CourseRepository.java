package com.gongkademy.repository;

import com.gongkademy.domain.Course;

public interface CourseRepository {
    public Course findById(Long courseId);
}
