package com.gongkademy.service;

import static com.gongkademy.exception.ErrorCode.REGISTERED_COURSE;
import static com.gongkademy.exception.ErrorCode.REGISTER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Register;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.RegisterRepository;
import com.gongkademy.service.dto.CourseDetailResponse;
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

    @Test
    void 강좌_상세_조회(){
        //Given
        Member member1 = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Member member2 = Member.builder().nickname("유저2").email("bbb@naver.com").build();
        Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
        Lecture lecture1 = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy").runtime(15 * 60)
                                  .course(course).build();
        Lecture lecture2 = Lecture.builder().title("응력이란").lectureOrder(2).url("y1b7jfIg_2w&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy&index=2").runtime(16 * 60)
                                  .course(course).build();
        em.persist(member1);
        em.persist(member2);
        em.persist(course);
        em.persist(lecture1);
        em.persist(lecture2);
        //When
        courseService.registerCourse(member1.getId(),course.getId());
        CourseDetailResponse registerCourseDetail = courseService.findCourseDetail(member1.getId(),course.getId());
        CourseDetailResponse unregisterCourseDetail = courseService.findCourseDetail(member2.getId(),course.getId());
        CourseDetailResponse nonLoginCourseDetail = courseService.findCourseDetail(null,course.getId());

        //Then
        //수강한 유저
        assertEquals(course.getTitle(), registerCourseDetail.getTitle());
        assertEquals(course.getThumbnail(), registerCourseDetail.getThumbnail());
        assertEquals(null, registerCourseDetail.getCourseNote());
        assertTrue(registerCourseDetail.isRegister());
        assertEquals(lecture1.getRuntime()+lecture2.getRuntime(), registerCourseDetail.getCourseTime());

        //수강하지 않은 유저
        assertEquals(course.getTitle(), unregisterCourseDetail.getTitle());
        assertEquals(course.getThumbnail(), unregisterCourseDetail.getThumbnail());
        assertEquals(null, unregisterCourseDetail.getCourseNote());
        assertFalse(unregisterCourseDetail.isRegister());
        assertEquals(lecture1.getRuntime()+lecture2.getRuntime(), unregisterCourseDetail.getCourseTime());

        //로그인 안한 유저
        assertEquals(course.getTitle(), nonLoginCourseDetail.getTitle());
        assertEquals(course.getThumbnail(), nonLoginCourseDetail.getThumbnail());
        assertEquals(null, nonLoginCourseDetail.getCourseNote());
        assertFalse(nonLoginCourseDetail.isRegister());
        assertEquals(lecture1.getRuntime()+lecture2.getRuntime(), nonLoginCourseDetail.getCourseTime());
    }

}
