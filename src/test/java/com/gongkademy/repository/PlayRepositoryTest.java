package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Play;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlayRepositoryTest {

    @Autowired EntityManager em;
    @Autowired PlayRepository playRepository;

    @Test
    void 강의_수강_기록_남기기(){
        //Given
        Member member1 = Member.builder()
                              .nickname("유저1")
                              .email("aaaa@naver.com")
                              .build();
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

        em.persist(course);
        em.persist(member1);
        em.persist(lecture1);

        //When
        Play play1 = Play.builder()
                        .member(member1)
                        .lecture(lecture1)
                        .lastPlayedTime(140)
                        .build();

        Long savedPlayId1 = playRepository.save(play1);

        //Then
        assertEquals(play1.getId(),savedPlayId1);
    }

    @Test
    void 강좌의_가장_최근_수강_강의기록_조회(){
        //Given
        Member member1 = Member.builder()
                               .nickname("유저1")
                               .email("aaaa@naver.com")
                               .build();
        Member member2 = Member.builder()
                               .nickname("유저2")
                               .email("aaas@naver.com")
                               .build();
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
        em.persist(member1);
        em.persist(member2);
        em.persist(lecture1);
        em.persist(lecture2);

        //When
        Play play1 = Play.builder()
                         .member(member1)
                         .lecture(lecture1)
                         .lastPlayedTime(140)
                         .build();
        Play play2 = Play.builder()
                         .member(member1)
                         .lecture(lecture2)
                         .lastPlayedTime(160)
                         .build();
        Play play3 = Play.builder()
                         .member(member2)
                         .lecture(lecture1)
                         .lastPlayedTime(180)
                         .build();
        Play play4 = Play.builder()
                         .member(member2)
                         .lecture(lecture2)
                         .lastPlayedTime(200)
                         .build();


        playRepository.save(play1);
        playRepository.save(play2);
        playRepository.save(play3);
        playRepository.save(play4);

        Play findPlay1 = playRepository.findByMemberIdAndCourseIdByModifiedTime(member1.getId(), course.getId());
        Play findPlay2 = playRepository.findByMemberIdAndCourseIdByModifiedTime(member2.getId(), course.getId());

        //Then
        assertEquals(lecture2.getId(),findPlay1.getLecture().getId());
        assertEquals(lecture2.getId(),findPlay2.getLecture().getId());

    }

}
