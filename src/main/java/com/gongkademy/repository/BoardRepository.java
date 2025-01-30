package com.gongkademy.repository;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository{

    List<Board> findAllByCategory(BoardCategory category,int page, BoardCriteria boardCriteria);
    Long countAllByCategory(BoardCategory category);
    Optional<Board> findById(Long boardId);
    List<Board> findAllByMemberId(Long memberId);
    Long save(Board board);
    Long update(Long boardId, Board board);
    Long delete(Board board);
}
