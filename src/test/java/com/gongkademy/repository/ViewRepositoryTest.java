package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.View;
import com.gongkademy.domain.board.Worry;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ViewRepositoryTest {

//    @Autowired
//    EntityManager em;
//    @Autowired
//    ViewRepository viewRepository;
//
//    @Test
//    void 조회_기록_저장() {
//        //given
//        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
//        Worry worry = Worry.builder().title("고민1").body("고민내용1").member(member).build();
//
//        em.persist(member);
//        em.persist(worry);
//
//        //When
//        View view = View.builder().board(worry).member(member).build();
//        viewRepository.save(view);
//        View findView = em.find(View.class, view.getId());
//        //Then
//        assertEquals(view, findView);
//    }
//
//    @Test
//    void 게시글_id로_조회기록_조회() {
//        //given
//        Member member1 = Member.builder().nickname("유저1").email("aaa@naver.com").build();
//        Member member2 = Member.builder().nickname("유저2").email("bbb@naver.com").build();
//        Worry worry = Worry.builder().title("고민1").body("고민내용1").member(member1).build();
//
//        em.persist(member1);
//        em.persist(member2);
//        em.persist(worry);
//        View view1 = View.builder().board(worry).member(member1).build();
//        View view2 = View.builder().board(worry).member(member2).build();
//
//        //When
//        viewRepository.save(view1);
//        viewRepository.save(view2);
//        List<View> findViewList = viewRepository.findAllByBoardId(worry.getId());
//        //Then
//        assertEquals(2, findViewList.size());
//    }
}
