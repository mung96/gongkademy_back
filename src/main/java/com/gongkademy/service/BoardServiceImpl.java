package com.gongkademy.service;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.LECTURE_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.NOT_BOARD_WRITER;
import static com.gongkademy.exception.ErrorCode.NOT_COMMENT_WRITER;

import com.gongkademy.domain.Comment;
import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.BoardRepository;
import com.gongkademy.repository.CommentRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LectureRepository lectureRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardListResponse findBoardList(BoardCategory category, int page) {
        //TODO: 페이징 처리, 일단 전체 조회로 구현
        List<Board> boardList = boardRepository.findAllByCategory(category);
        List<BoardItemDto> boardItemDtoList = new ArrayList<>();

        for(Board board:boardList){
            List<Comment> commentList = commentRepository.findByBoardId(board.getId());
            boardItemDtoList.add(BoardItemDto.builder()
                    .title(board.getTitle())
                    .body(board.getBody())
                    .date(board.getModifiedTime().toString())
                     .courseTitle(category == QUESTION ? ((Question)board).getLecture().getCourse().getTitle() : null)
                     .commentCount(commentList.size())
                    .build());
        }

        return BoardListResponse.builder().boardList(boardItemDtoList).build();
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse findBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomException(BOARD_NOT_FOUND));
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        List<CommentItemDto> commentDtoList = new ArrayList<>();
        for(Comment comment:commentList){
            commentDtoList.add(CommentItemDto.builder()
                                             .content(comment.getContent())
                                             .nickname(comment.getMember().getNickname())
                                             .date(comment.getModifiedTime().toString())
                                             .build());
        }
        return BoardDetailResponse.builder()
                .title(board.getTitle())
                .body(board.getBody())
                .date(board.getModifiedTime().toString())
                .nickname(board.getMember().getNickname())
                .commentList(commentDtoList)
                .courseTitle(board instanceof Question ? ((Question)board).getLecture().getCourse().getTitle() : null)
                .lectureTitle(board instanceof Question ? ((Question)board).getLecture().getTitle() : null)
                .build();
    }

    @Override
    public Long write(Long memberId, WriteBoardRequest board, BoardCategory category) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Board newBoard = null;
        if (category == BoardCategory.WORRY) {
            newBoard = Worry.builder()
                                     .title(board.getTitle())
                                     .body(board.getBody())
                                     .member(member)
                                     .build();

        } else if (category == QUESTION) {
            Lecture lecture = lectureRepository.findById(board.getLectureId()).orElseThrow(() -> new CustomException(LECTURE_NOT_FOUND));
            newBoard = Question.builder()
                                        .title(board.getTitle())
                                        .body(board.getBody())
                                        .member(member)
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
        if(category == QUESTION && newBoard.getLectureId() != null){
            Lecture lecture = lectureRepository.findById(newBoard.getLectureId()).orElseThrow(()->new CustomException(LECTURE_NOT_FOUND));
            ((Question)board).changeLecture(lecture);
        }

        return board.getId();
    }


    @Override
    public Long delete(Long memberId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CustomException(BOARD_NOT_FOUND));
        if(!board.getMember().getId().equals(memberId)) throw new CustomException(NOT_BOARD_WRITER);

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
        return newComment.getId();
    }

    @Override
    public Long deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CustomException(COMMENT_NOT_FOUND));
        if(!comment.getMember().getId().equals(memberId)) throw new CustomException(NOT_COMMENT_WRITER);

        return commentRepository.delete(comment);
    }
}
