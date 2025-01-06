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
    void id로_회원_조회(){
        //given
        Member member = Member.builder()
                              .nickname("유저1")
                              .email("aaa@naver.com")
                              .build();
        //when
        em.persist(member);
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        assertEquals(member,findMember);
    }

    @Test
    void 닉네임으로_회원_조회(){
        //given
        Member member = Member.builder()
                              .nickname("유저1")
                              .email("aaa@naver.com")
                              .build();
        //when
        em.persist(member);
        Member findMember = memberRepository.findByNickname(member.getNickname()).get();

        //then
        assertEquals(member,findMember);
    }

    @Test
    void findByProviderAndProviderId() {
        //TOOD:작성해야함
    }
}
