package com.gongkademy.repository;

import com.gongkademy.domain.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long commentId);
    List<Comment> findByBoardId(Long boardId);
    Long save(Comment comment);
    Long delete(Comment comment);
}
