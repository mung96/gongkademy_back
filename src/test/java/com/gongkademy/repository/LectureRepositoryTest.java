package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LectureRepositoryTest {

    @Autowired EntityManager em;
    @Autowired LectureRepository lectureRepository;

    @Test
    void 강좌id로_강의목록_조회(){
        //given
        Course course = Course.builder()
                              .title("재료역학")
                              .thumbnail("aaa")
                              .build();
        Lecture lecture1 = Lecture.builder()
                                 .title("재료역학이란")
                                 .lectureOrder(1)
                                 .url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                 .runtime(15 * 60)
                                  .course(course)
                                 .build();
        Lecture lecture2 = Lecture.builder()
                                 .title("응력이란")
                                 .lectureOrder(2)
                                 .url("y1b7jfIg_2w&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy&index=2")
                                 .runtime(16 * 60)
                                  .course(course)
                                 .build();
        em.persist(course);
        em.persist(lecture1);
        em.persist(lecture2);

        //when
        List<Lecture> findLectures = lectureRepository.findLecturesByCourseId(1L);

        //then
        assertEquals(2,findLectures.size());
        assertTrue(findLectures.contains(lecture1));
        assertTrue(findLectures.contains(lecture2));
    }

}
