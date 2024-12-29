package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Course;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Register;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RegisterRepositoryTest {

    @Autowired EntityManager em;
    @Autowired
    RegisterRepository registerRepository;

    @Test
    void 강좌_수강_신청(){
        //Given
        Member member = Member.builder()
                              .nickname("유저1")
                              .email("aaaa@naver.com")
                              .build();
        Course course = Course.builder()
                              .title("재료역학")
                              .thumbnail("aaa")
                              .build();
        em.persist(course);
        em.persist(member);

        Register register = Register.builder()
                                  .member(member)
                                  .course(course)
                                  .build();
        //When
        Long findRegistId = registerRepository.save(register);
        Register findRegister = registerRepository.findByMemberIdAndCourseId(member.getId(), course.getId()).get();

        //Then
        assertEquals(register.getId(),findRegistId);
        assertEquals(findRegister,register);
    }

    @Test
    void 회원id_강좌id_강좌_수강_철회(){
        //given
        //Given
        Member member = Member.builder()
                              .nickname("유저1")
                              .email("aaaa@naver.com")
                              .build();
        Course course = Course.builder()
                              .title("재료역학")
                              .thumbnail("aaa")
                              .build();
        em.persist(course);
        em.persist(member);

        Register register = Register.builder()
                                  .member(member)
                                  .course(course)
                                  .build();
        //When
        Long findRegistId = registerRepository.save(register);
        Long deletedRegistId = registerRepository.deleteById(member.getId(), course.getId());

        //then
        assertEquals(register.getId(),deletedRegistId);
    }

}
