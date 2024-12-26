package com.gongkademy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Comment;
import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    CommentRepository commentRepository;

    @Test
    void 댓글_작성(){
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Worry worry = Worry.builder().title("고민1").body("고민내용1").member(member).build();

        em.persist(member);
        em.persist(worry);

        Comment comment = Comment.builder().content("댓글내용1").member(member).board(worry).build();

        //when
        commentRepository.save(comment);
        Comment findComment = em.find(Comment.class,comment.getId());

        //then
       assertEquals(findComment,comment);
    }

    @Test
    void 댓글_삭제(){
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Worry worry = Worry.builder().title("고민1").body("고민내용1").member(member).build();

        em.persist(member);
        em.persist(worry);

        Comment comment = Comment.builder().content("댓글내용1").member(member).board(worry).build();
        em.persist(comment);

        //when
        Long deleteCommentId =  commentRepository.delete(comment.getId());

        //then
        assertEquals(comment.getId(),deleteCommentId);
    }

}
