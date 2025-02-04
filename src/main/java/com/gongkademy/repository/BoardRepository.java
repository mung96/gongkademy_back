package com.gongkademy.repository;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository{

    //댓글순 상위 4개 조회
//    List<Board> findTop4ByOrderByCommentCntDesc();

    List<Board> findAllByCategory(BoardCategory category,int page, BoardCriteria boardCriteria);
    List<Board> findAllByCategoryAndMemberId(Long memberId,BoardCategory category,int page, BoardCriteria boardCriteria);
    Long countAllByCategoryAndMemberId(Long memberId,BoardCategory category);
    Long countAllByCategory(BoardCategory category);
    Optional<Board> findById(Long boardId);
    List<Board> findAllByMemberId(Long memberId);
    Long save(Board board);
    Long update(Long boardId, Board board);
    Long delete(Board board);
}
