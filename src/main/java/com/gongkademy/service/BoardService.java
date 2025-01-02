package com.gongkademy.service;

import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.service.dto.BoardDetailResponse;
import com.gongkademy.service.dto.BoardListResponse;
import com.gongkademy.service.dto.EditBoardRequest;
import com.gongkademy.service.dto.WriteBoardRequest;

public interface BoardService {

    BoardListResponse findBoardList(BoardCategory category, int page);

    BoardDetailResponse findBoardDetail(Long boardId);

    Long write(Long memberId, WriteBoardRequest board, BoardCategory category);

    Long edit(Long memberId, Long boardId, EditBoardRequest board, BoardCategory category);

    Long delete(Long memberId, Long boardId);
}
