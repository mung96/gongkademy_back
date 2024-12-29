package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CourseRepositoryTest {

    @Autowired EntityManager em;
    @Autowired CourseRepository courseRepository;

    @Test
    void id로_강좌_조회() {

        //given
        Course course = Course.builder()
                              .title("재료역학")
                              .thumbnail("aaa")
                              .build();
        em.persist(course);

        //when
        Course findCourse = courseRepository.findById(1L).get();

        //then
        assertEquals("재료역학",findCourse.getTitle());
        assertEquals("aaa",findCourse.getThumbnail());
    }
}
