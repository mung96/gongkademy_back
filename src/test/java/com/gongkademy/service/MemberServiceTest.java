package com.gongkademy.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Member;
import com.gongkademy.service.dto.UpdateProfileRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    EntityManager em;
    @Autowired MemberService memberService;

    @Test
    void 회원_프로필_수정(){
        //given
        Member member = Member.builder()
                              .nickname("유저1")
                              .email("aaa@naver.com")
                              .build();
        //when
        em.persist(member);
        memberService.updateProfile(member.getId(),new UpdateProfileRequest("유저2"));
        Member findMember = em.find(Member.class,member.getId());

        //then
        assertEquals("유저2",findMember.getNickname());
    }

    @Test
    void 닉네임_중복_예외발생(){

    }
}
