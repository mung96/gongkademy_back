package com.gongkademy.service;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.LECTURE_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.NOT_BOARD_WRITER;
import static com.gongkademy.exception.ErrorCode.NOT_COMMENT_WRITER;
import static com.gongkademy.exception.ErrorCode.NOT_VALID_QUESTION_REQUEST;

import com.gongkademy.domain.board.Comment;
import com.gongkademy.domain.course.Course;
import com.gongkademy.domain.course.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.BoardRepository;
import com.gongkademy.repository.CommentRepository;
import com.gongkademy.repository.CourseRepository;
import com.gongkademy.repository.LectureRepository;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.service.dto.BoardDetailResponse;
import com.gongkademy.service.dto.BoardItemDto;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.CommentItemDto;
import com.gongkademy.service.dto.EditBoardRequest;
import com.gongkademy.service.dto.WriteBoardRequest;
import com.gongkademy.service.dto.WriteCommentRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardListResponse findBoardList(BoardCategory boardCategory, int page, BoardCriteria boardCriteria) {

        List<BoardItemDto> boardList = boardRepository.findAllByCategory(boardCategory,page,boardCriteria).stream().map(board -> BoardItemDto.builder()
                                                                    .boardCategory(board.getBoardCategory())
                                                                     .boardId(board.getId())
                                                                     .title(board.getTitle())
                                                                     .body(board.getBody())
                                                                     .date(board.getUpdatedAt().toString())
                                                                     .courseTitle(boardCategory == QUESTION ? ((Question)board).getCourse().getTitle() : null)
                                                                     .lectureTitle((boardCategory == QUESTION && ((Question)board).getLecture() != null) ?((Question)board).getLecture().getTitle() : null)
                                                                     .commentCount(commentRepository.findByBoardId(board.getId()).size())
                                                                     .build()).toList();

        Long totalPage = boardRepository.countAllByCategory(boardCategory);

        return BoardListResponse.builder().boardList(boardList).totalPage(totalPage).build();
    }

    @Override
    public BoardListResponse findQuestionList( int page, BoardCriteria boardOrder, Long courseId, Long lectureId) {
        List<BoardItemDto> boardList = boardRepository.findAllQuestionByCourseIdAndLectureId(courseId,lectureId,page).stream().map(question -> BoardItemDto.builder()
                                                                                                                         .boardCategory(question.getBoardCategory())
                                                                                                                         .boardId(question.getId())
                                                                                                                         .title(question.getTitle())
                                                                                                                         .body(question.getBody())
                                                                                                                         .date(question.getUpdatedAt().toString())
                                                                                                                         .courseTitle(question.getCourse().getTitle())
                                                                                                                         .lectureTitle(question.getLecture() != null?question.getLecture().getTitle() : null)
                                                                                                                         .commentCount(commentRepository.findByBoardId(question.getId()).size())
                                                                                                                         .build()).toList();

        Long totalPage = boardRepository.countAllQuestionByCourseIdAndLectureId(courseId,lectureId);

        return BoardListResponse.builder().boardList(boardList).totalPage(totalPage).build();
    }

    //TODO: 댓글 불러오는거랑 게시글 상세 불러오는게 분리가 되어야하네. 그래야 프론트에서 댓글변경시 그거만 불러오는게 가능
    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse findBoardDetail(Long memberId,Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomException(BOARD_NOT_FOUND));
        log.info("게시판 상세 조회: {}",board);
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        List<CommentItemDto> commentDtoList = new ArrayList<>();
        for(Comment comment:commentList){
            commentDtoList.add(CommentItemDto.builder()
                                            .commentId(comment.getId())
                                            .isMine(memberId.equals(comment.getMember().getId()))
                                             .content(comment.getContent())
                                             .nickname(comment.getMember().getNickname())
                                             .date(comment.getUpdatedAt().toString())
                                             .build());
        }
        return BoardDetailResponse.builder()
                .boardCategory(board.getBoardCategory())
                .boardId(board.getId())
                .isMine(memberId.equals(board.getMember().getId()))
                .title(board.getTitle())
                .body(board.getBody())
                .date(board.getUpdatedAt().toString())
                .nickname(board.getMember().getNickname())
                .commentList(commentDtoList)
                .courseTitle(board.getBoardCategory() == QUESTION ? ((Question)board).getCourse().getTitle() : null)
                .lectureTitle((board.getBoardCategory() == QUESTION && ((Question)board).getLecture() != null) ?((Question)board).getLecture().getTitle() : null)
                .build();
    }

    @Override
    public Long write(Long memberId, WriteBoardRequest board, BoardCategory category) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        if(category == QUESTION && board.getCourseId() == null) throw new CustomException(NOT_VALID_QUESTION_REQUEST);

        Board newBoard = null;
        if (category == BoardCategory.WORRY) {
            newBoard = Worry.builder()
                                     .title(board.getTitle())
                                     .body(board.getBody())
                                     .member(member)
                                     .build();

        } else if (category == QUESTION) {
            Lecture lecture = null;
            if(board.getLectureId() != null){
                lecture = lectureRepository.findById(board.getLectureId()).orElseThrow(() -> new CustomException(LECTURE_NOT_FOUND));
            }

            Course course = courseRepository.findById(board.getCourseId()).orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
            newBoard = Question.builder()
                                        .title(board.getTitle())
                                        .body(board.getBody())
                                        .member(member)
                                        .course(course)
                                        .lecture(lecture)
                                        .build();
        }

        if(newBoard == null) throw new CustomException(ErrorCode.BOARD_CATEGORY_NOT_FOUND);
        boardRepository.save(newBoard);

        return newBoard.getId();
    }

    @Override
    public Long edit(Long memberId, Long boardId, EditBoardRequest newBoard, BoardCategory category) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomException(BOARD_NOT_FOUND));
        if(!board.getMember().getId().equals(memberId)) throw new CustomException(NOT_BOARD_WRITER);

        if(newBoard.getTitle() != null){
            board.changeTitle(newBoard.getTitle());
        }
        if(newBoard.getBody() != null){
            board.changeBody(newBoard.getBody());
        }
        if(category == QUESTION){
            Lecture lecture = lectureRepository.findById(newBoard.getLectureId()).orElseThrow(()->new CustomException(LECTURE_NOT_FOUND));
            Course course = courseRepository.findById(newBoard.getCourseId()).orElseThrow(()->new CustomException(ErrorCode.COURSE_NOT_FOUND));
            ((Question)board).changeLecture(lecture);
            ((Question)board).changeCourse(course);
        }

        return board.getId();
    }


    @Override
    public Long delete(Long memberId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomException(BOARD_NOT_FOUND));
        if(!board.getMember().getId().equals(memberId)) throw new CustomException(NOT_BOARD_WRITER);

        log.info("게시판 삭제: {}",boardId);
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        for(Comment comment:commentList){
            commentRepository.delete(comment);
        }
        log.info("게시판 삭제 완료: {}",boardId);
        return boardRepository.delete(board);
    }

    @Override
    public Long writeComment(Long memberId, Long boardId, WriteCommentRequest comment) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomException(BOARD_NOT_FOUND));

        Comment newComment = Comment.builder()
                                    .content(comment.getContent())
                                    .member(member)
                                    .board(board)
                                    .build();
        commentRepository.save(newComment);
        return newComment.getId();
    }

    @Override
    public Long deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CustomException(COMMENT_NOT_FOUND));
        if(!comment.getMember().getId().equals(memberId)) throw new CustomException(NOT_COMMENT_WRITER);

        return commentRepository.delete(comment);
    }
}
