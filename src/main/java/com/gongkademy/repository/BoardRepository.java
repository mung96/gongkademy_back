package com.gongkademy.repository;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    List<Board> findAllByCategory(BoardCategory category);
    Optional<Board> findById(Long boardId);
    List<Board> findAllByMemberId(Long memberId);
    Long save(Board board);
    Long update(Long boardId, Board board);
    Long delete(Board board);
}
