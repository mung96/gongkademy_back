package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired BoardRepository boardRepository;

    @Test
    void 게시글_id로_조회(){
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
        Lecture lecture = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                  .runtime(15 * 60).course(course).build();
        Question question = Question.builder().title("질문1").body("질문내용1").lecture(lecture).build();
        Worry worry = Worry.builder().title("고민1").body("고민내용1").build();

        em.persist(member);
        em.persist(course);
        em.persist(lecture);
        em.persist(question);
        em.persist(worry);

        //when
        Board findQuestion = boardRepository.findById(question.getId());
        Board findWorry = boardRepository.findById(worry.getId());

        //then
        assertEquals(question,findQuestion);
        assertEquals(worry,findWorry);
    }
}
