package com.gongkademy.service;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.LECTURE_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.gongkademy.exception.ErrorCode.NOT_WRITER;

import com.gongkademy.domain.Lecture;
import com.gongkademy.domain.Member;
import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.Question;
import com.gongkademy.domain.board.Worry;
import com.gongkademy.exception.CustomException;
import com.gongkademy.exception.ErrorCode;
import com.gongkademy.repository.BoardRepository;
import com.gongkademy.repository.LectureRepository;
import com.gongkademy.repository.MemberRepository;
import com.gongkademy.service.dto.EditBoardRequest;
import com.gongkademy.service.dto.WriteBoardRequest;
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
        if(!board.getMember().getId().equals(memberId)) throw new CustomException(NOT_WRITER);

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
        if(!board.getMember().getId().equals(memberId)) throw new CustomException(NOT_WRITER);

        return boardRepository.delete(board);
    }
}
