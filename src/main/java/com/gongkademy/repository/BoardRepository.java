package com.gongkademy.repository;

import com.gongkademy.domain.board.Board;
import java.util.List;

public interface BoardRepository {

    Board findById(Long boardId);
    List<Board> findAllByMemberId(Long memberId);
    Long save(Board board);
    Long update(Long boardId, Board board);
    Long delete(Long boardId);
}
