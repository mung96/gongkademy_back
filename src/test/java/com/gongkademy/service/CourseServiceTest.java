package com.gongkademy.service;

import static com.gongkademy.exception.ErrorCode.REGISTERED_COURSE;
import static com.gongkademy.exception.ErrorCode.REGISTER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Register;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.RegisterRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired CourseService courseService;
    @Autowired EntityManager em;
    @Autowired
    RegisterRepository registerRepository;

    @Test
    void 수강_신청() {
        //Given
        Member member = Member.builder()
                               .nickname("유저1")
                               .email("aaas@naver.com")
                               .build();
        Course course = Course.builder()
                              .title("재료역학")
                              .thumbnail("aaa")
                              .build();
        em.persist(member);
        em.persist(course);


        //When
        Long registerCourseId = courseService.registerCourse(member.getId(), course.getId());
        Register findRegister = em.find(Register.class, registerCourseId);

        CustomException e = assertThrows(CustomException.class, () -> {
            courseService.registerCourse(member.getId(), course.getId());
        });

        //Then
        assertEquals(findRegister.getId(), registerCourseId);
        assertEquals(REGISTERED_COURSE,e.getErrorCode());
    }

    @Test
    void 수강_취소() {
        //Given
        Member member1 = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Member member2 = Member.builder().nickname("유저2").email("bbb@naver.com").build();
        Course course = Course.builder()
                              .title("재료역학")
                              .thumbnail("aaa")
                              .build();
        em.persist(member1);
        em.persist(member2);
        em.persist(course);

        //when
        Long registerCourseId1 = courseService.registerCourse(member1.getId(), course.getId());

        Long deletedCourseId = courseService.dropCourse(member1.getId(), course.getId());
        CustomException e = assertThrows(CustomException.class, () -> {
            courseService.dropCourse(member2.getId(), course.getId());
        });

        //Then
        assertEquals(registerCourseId1, deletedCourseId);
        assertEquals(REGISTER_NOT_FOUND, e.getErrorCode());
    }

}
