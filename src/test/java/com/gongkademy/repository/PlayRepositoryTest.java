package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.course.Course;
import com.gongkademy.domain.course.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.course.Play;
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
        Play firstPlayByMember1 = Play.builder()
                         .member(member1)
                         .lecture(lecture1)
                         .lastPlayedTime(140)
                         .build();
        Play lastPlayByMember1 = Play.builder()
                         .member(member1)
                         .lecture(lecture2)
                         .lastPlayedTime(160)
                         .build();
        Play firstPlayByMember2 = Play.builder()
                         .member(member2)
                         .lecture(lecture1)
                         .lastPlayedTime(180)
                         .build();
        Play lastPlayByMember2 = Play.builder()
                                     .member(member2)
                                     .lecture(lecture2)
                                     .lastPlayedTime(250)
                                     .build();


        playRepository.save(firstPlayByMember1);
        playRepository.save(lastPlayByMember1);
        playRepository.save(firstPlayByMember2);
        playRepository.save(lastPlayByMember2);

        Play findedLastPlayByMember1AndCourseId = playRepository.findByMemberIdAndCourseIdByModifiedTime(member1.getId(), course.getId()).get();
        Play findedLastPlayByMember2AndCourseId = playRepository.findByMemberIdAndCourseIdByModifiedTime(member2.getId(), course.getId()).get();

        Play findedLastPlayByMember1AndLectureId = playRepository.findByMemberIdAndLectureId(member1.getId(), lecture1.getId()).get();
        Play findedLastPlayByMember2AndLectureId = playRepository.findByMemberIdAndLectureId(member2.getId(), lecture1.getId()).get();

        //Then
        //회원1이 강좌에서 가장 최근 수강한 강의
        assertEquals(lecture2.getId(),findedLastPlayByMember1AndCourseId.getLecture().getId());
        assertEquals(lastPlayByMember1.getLastPlayedTime(),findedLastPlayByMember1AndCourseId.getLastPlayedTime());

        //회원2가 강좌에서 가장 최근 수강한 강의
        assertEquals(lecture2.getId(),findedLastPlayByMember2AndCourseId.getLecture().getId());
        assertEquals(lastPlayByMember2.getLastPlayedTime(),findedLastPlayByMember2AndCourseId.getLastPlayedTime());

        //회원1이 강의1에서 가장 최근 수강기록
        assertEquals(lecture1.getId(),findedLastPlayByMember1AndLectureId.getLecture().getId());
        assertEquals(firstPlayByMember1.getLastPlayedTime(),findedLastPlayByMember1AndLectureId.getLastPlayedTime());

        //회원2가 강의1에서 가장 최근 수강기록
        //TODO: service에서 update query적용해야함.
        assertEquals(lecture1.getId(),findedLastPlayByMember2AndLectureId.getLecture().getId());
        assertEquals(firstPlayByMember2.getLastPlayedTime(),findedLastPlayByMember2AndLectureId.getLastPlayedTime());
    }
}
