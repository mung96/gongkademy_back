package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원_프로필_수정(){
        //given
        Member member = Member.builder()
                              .nickname("유저1")
                              .email("aaa@naver.com")
                              .build();
        //when
        em.persist(member);
        Long memberId = memberRepository.update(member.getEmail(),Member.builder().nickname("유저2").build());
        Member findMember = em.find(Member.class,memberId);

        //then
        assertEquals("유저2",findMember.getNickname());
        assertEquals(member.getId(),findMember.getId());

    }
}
