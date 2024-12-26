package com.gongkademy.repository;

import com.gongkademy.domain.Comment;
import com.gongkademy.domain.board.Board;
import java.util.List;

public interface CommentRepository {
    List<Comment> findByBoardId(Long boardId);
    Long save(Comment comment);
    Long delete(Long commentId);
}
