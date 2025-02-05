package com.gongkademy.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Member;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.service.dto.UpdateProfileDto;
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
        memberService.updateProfile(member.getId(),new UpdateProfileDto("유저2"));
        Member findMember = em.find(Member.class,member.getId());

        //then
        assertEquals("유저2",findMember.getNickname());
    }

    @Test
    void 닉네임_중복_예외발생(){
        //given
        Member defaultMember = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Member member = Member.builder().nickname("유저2").email("bbb@naver.com").build();

        //when
        em.persist(defaultMember);
        em.persist(member);
        CustomException e = assertThrows(CustomException.class,
                                       () -> memberService.updateProfile(member.getId(),new UpdateProfileDto("유저1")));

        assertEquals(e.getErrorCode(),ErrorCode.DUPLICATED_NICKNAME);
    }
}
