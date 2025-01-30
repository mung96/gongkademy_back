package com.gongkademy.service;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.domain.board.BoardCategory.WORRY;
import static com.gongkademy.exception.ErrorCode.NOT_BOARD_WRITER;
import static org.junit.jupiter.api.Assertions.*;

import com.gongkademy.domain.Comment;
import com.gongkademy.domain.Course;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.Provider;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import com.gongkademy.exception.CustomException;
import com.gongkademy.service.dto.BoardDetailResponse;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.EditBoardRequest;
import com.gongkademy.service.dto.WriteBoardRequest;
import com.gongkademy.service.dto.WriteCommentRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired EntityManager em;
    @Test
    void 게시글_페이징_조회() {
        //given
        Member member = Member.builder().name("유저1").provider(Provider.NAVER).providerId("aaa").nickname("유저1").email("aaa@naver.com").build();
        Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
        Lecture lecture = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                 .runtime(15 * 60).course(course).build();
        em.persist(member);
        em.persist(course);
        em.persist(lecture);
        Question question = Question.builder().title("질문0").body("질문내용0").member(member).lecture(lecture).build();
        Worry worry = Worry.builder().title("고민0").body("고민내용0").member(member).build();
        em.persist(question);
        em.persist(worry);

        for(int i=0;i<100;i++){
            Question question1 = Question.builder().title("질문1").body("질문내용1").member(member).lecture(lecture).build();
            Worry worry1 = Worry.builder().title("고민1").body("고민내용1").member(member).build();
            em.persist(question1);
            em.persist(worry1);
        }

        //when
        BoardListResponse questionListResponse =  boardService.findBoardList(QUESTION, 1, BoardCriteria.CREATED_AT);
        BoardListResponse worryListResponse =  boardService.findBoardList(WORRY, 1, BoardCriteria.CREATED_AT);

        //then
        assertEquals(20,questionListResponse.getBoardList().size());
        assertEquals(20,worryListResponse.getBoardList().size());
        assertEquals(6,questionListResponse.getTotalPage());
        assertEquals(6,worryListResponse.getTotalPage());
        assertEquals("질문0",questionListResponse.getBoardList().get(0).getTitle());
        assertEquals("고민0",worryListResponse.getBoardList().get(0).getTitle());

    }
    @Test
    void 게시글_상세_조회(){
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
        Lecture lecture = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                 .runtime(15 * 60).course(course).build();

        em.persist(member);
        em.persist(course);
        em.persist(lecture);

        WriteBoardRequest questionRequest = WriteBoardRequest.builder().title("질문1").body("질문내용1").lectureId(lecture.getId()).build();
        Long questionId = boardService.write(member.getId(),questionRequest , QUESTION);
        WriteCommentRequest comment1 = WriteCommentRequest.builder().content("댓글1").build();
        WriteCommentRequest comment2 = WriteCommentRequest.builder().content("댓글2").build();


        //when
        Long comment1Id =  boardService.writeComment(member.getId(),questionId, comment1);
        Long comment2Id = boardService.writeComment(member.getId(),questionId, comment2);
        boardService.deleteComment(member.getId(),comment2Id);
        BoardDetailResponse findQuestion = boardService.findBoardDetail(questionId);


        //then
        assertEquals(questionRequest.getTitle(),findQuestion.getTitle());
        assertEquals(questionRequest.getBody(),findQuestion.getBody());
        assertEquals(member.getNickname(),findQuestion.getNickname());
        assertEquals(course.getTitle(),findQuestion.getCourseTitle());
        assertEquals(lecture.getTitle(),findQuestion.getLectureTitle());
        assertEquals(1,findQuestion.getCommentList().size());
    }

    @Test
    void 게시글_작성(){
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
        Lecture lecture = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                 .runtime(15 * 60).course(course).build();

        em.persist(member);
        em.persist(course);
        em.persist(lecture);

        WriteBoardRequest worryRequest = WriteBoardRequest.builder().title("고민1").body("고민내용1").build();
        WriteBoardRequest questionRequest = WriteBoardRequest.builder().title("질문1").body("질문내용1").lectureId(lecture.getId()).build();
        //when
        Long worryId = boardService.write(member.getId(),worryRequest ,WORRY);
        Long questionId = boardService.write(member.getId(),questionRequest , QUESTION);

        Question findQuestion = em.find(Question.class,questionId);
        Worry findWorry = em.find(Worry.class,worryId);
//
        //then
        assertEquals(questionRequest.getTitle(),findQuestion.getTitle());
        assertEquals(questionRequest.getLectureId(),findQuestion.getLecture().getId());
        assertEquals(worryRequest.getTitle(),findWorry.getTitle());
    }
    @Test
    void 게시글_수정(){
        //given
        Member member = Member.builder().nickname("유저1").email("aaa@naver.com").build();
        Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
        Lecture lecture1 = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                 .runtime(15 * 60).course(course).build();
        Lecture lecture2 = Lecture.builder().title("재료역학이란").lectureOrder(2).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                 .runtime(15 * 60).course(course).build();

        em.persist(member);
        em.persist(course);
        em.persist(lecture1);
        em.persist(lecture2);

        WriteBoardRequest questionRequest = WriteBoardRequest.builder().title("질문1").body("질문내용1").lectureId(lecture1.getId()).build();
        //when
        Long questionId = boardService.write(member.getId(),questionRequest , QUESTION);

        boardService.edit(member.getId(), questionId, EditBoardRequest.builder().body("고민내용2").lectureId(lecture2.getId()).build(), QUESTION);
        Question findQuestion = em.find(Question.class,questionId);

        //then
        assertEquals("질문1",findQuestion.getTitle());
        assertEquals("고민내용2",findQuestion.getBody());
        assertEquals(lecture2.getId(),findQuestion.getLecture().getId());
    }

    @Test
    void 게시글_삭제(){
            //given
            Member member1 = Member.builder().nickname("유저1").email("aaa@naver.com").build();
            Member member2 = Member.builder().nickname("유저2").email("bbb@naver.com").build();
            Course course = Course.builder().title("재료역학").thumbnail("aaa").build();
            Lecture lecture = Lecture.builder().title("재료역학이란").lectureOrder(1).url("ys5niu4Sabg&list=PLwzYFiJ0Ed6kGyX0M_IW1LGiLO_Ggh4Jy")
                                     .runtime(15 * 60).course(course).build();

            em.persist(member1);
            em.persist(member2);
            em.persist(course);
            em.persist(lecture);

            WriteBoardRequest worryRequest = WriteBoardRequest.builder().title("고민1").body("고민내용1").build();
            WriteBoardRequest questionRequest = WriteBoardRequest.builder().title("질문1").body("질문내용1").lectureId(lecture.getId()).build();
            //when
            Long worryId = boardService.write(member1.getId(),worryRequest ,WORRY);
            Long questionId = boardService.write(member1.getId(),questionRequest , QUESTION);

            boardService.delete(member1.getId(),worryId);
            CustomException e = assertThrows(CustomException.class, () -> {
                boardService.delete(member2.getId(), questionId);
            });

            Worry findWorry = em.find(Worry.class,worryId);

            //then
            assertNull(findWorry);
            assertEquals(NOT_BOARD_WRITER,e.getErrorCode());
        }
}
