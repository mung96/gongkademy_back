package com.gongkademy.repository;

import com.gongkademy.domain.Course;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository{

    private final EntityManager em;

    @Override
    public Course findById(Long courseId) {
        return em.find(Course.class,courseId);
    }
}
