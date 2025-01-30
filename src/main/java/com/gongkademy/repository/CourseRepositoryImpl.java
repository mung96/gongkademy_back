package com.gongkademy.repository;

import com.gongkademy.domain.course.Course;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository{

    private final EntityManager em;

    @Override
    public Optional<Course> findById(Long courseId) {
        return Optional.ofNullable(em.find(Course.class, courseId));
    }
}
