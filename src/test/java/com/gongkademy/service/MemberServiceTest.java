package com.gongkademy.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.Provider;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.service.dto.UpdateProfileDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {
//
//    @Autowired EntityManager em;
//    @Autowired MemberService memberService;
//
//    Member defaultMember;
//    Member anotherMember;
//
//    @BeforeEach
//    void setUp() {
//        // 기본 회원 2명 세팅
//        defaultMember = Member.builder().name("유저1").provider(Provider.KAKAO).providerId("aaa").nickname("유저1").email("aaa@naver.com").build();
//
//        anotherMember = Member.builder().name("유저2").provider(Provider.KAKAO).providerId("bbb").nickname("유저2").email("bbb@naver.com").build();
//
//        em.persist(defaultMember);
//        em.persist(anotherMember);
//    }
//
//    @Test
//    void 회원_프로필_수정() {
//        // when
//        memberService.updateProfile(defaultMember.getId(), new UpdateProfileDto("유저3"));
//        Member findMember = em.find(Member.class, defaultMember.getId());
//
//        // then
//        assertEquals("유저3", findMember.getNickname());
//    }
//
//    @Test
//    void 닉네임_중복_예외발생() {
//        // when & then
//        CustomException e = assertThrows(CustomException.class, () ->
//                memberService.updateProfile(anotherMember.getId(), new UpdateProfileDto("유저1")) // 이미 존재하는 닉네임
//        );
//        assertEquals(ErrorCode.DUPLICATED_NICKNAME, e.getErrorCode());
//    }
}
