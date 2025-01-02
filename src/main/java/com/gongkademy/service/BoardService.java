package com.gongkademy.service;

import com.gongkademy.domain.board.Board;
import com.gongkademy.service.dto.WriteBoardRequest;

public interface BoardService {

    Long write(Long memberId, WriteBoardRequest board);
    Long edit(Long memberId, WriteBoardRequest board);
    Long delete(Long memberId, Long boardId);
}
