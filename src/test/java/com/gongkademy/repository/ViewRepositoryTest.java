package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.View;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ViewRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    ViewRepository viewRepository;

    @Test
    void 조회_기록_저장() {
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Worry worry = Worry.builder().title("고민1").body("고민내용1").member(member).build();

        em.persist(member);
        em.persist(worry);

        //When
        View view = View.builder().board(worry).member(member).build();
        viewRepository.save(view);
        View findView = em.find(View.class, view.getId());
        //Then
        assertEquals(view, findView);
    }
}
