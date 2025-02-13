package com.gongkademy.service;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.service.dto.BoardDetailResponse;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.EditBoardRequest;
import com.gongkademy.service.dto.WriteBoardRequest;
import com.gongkademy.service.dto.WriteCommentRequest;

public interface BoardService {

    BoardListResponse findBoardList(BoardCategory category, int page, BoardCriteria boardOrder);

    BoardListResponse findQuestionList(int page, BoardCriteria boardOrder, Long courseId, Long lectureId);

    BoardListResponse findBoardListByKeyword(BoardCategory boardCategory, String keyword, int page);
    BoardListResponse findQuestionListByKeyword(String keyword, int page, Long courseId, Long lectureId);

    BoardDetailResponse findBoardDetail(Long memberId,Long boardId);

    Long write(Long memberId, WriteBoardRequest board, BoardCategory category);

    Long edit(Long memberId, Long boardId, EditBoardRequest board, BoardCategory category);

    Long delete(Long memberId, Long boardId);

    Long writeComment(Long memberId, Long boardId, WriteCommentRequest comment);

    Long deleteComment(Long memberId, Long commentId);
}
