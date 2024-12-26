package com.gongkademy.repository;

import com.gongkademy.domain.Comment;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final EntityManager em;

    @Override
    public List<Comment> findByBoardId(Long boardId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.board.id = :boardId",Comment.class)
                .setParameter("boardId",boardId)
                .getResultList();
    }

    @Override
    public Long save(Comment comment) {
        em.persist(comment);
        return comment.getId();
    }

    @Override
    public Long delete(Long commentId) {
        Comment findComment = em.find(Comment.class,commentId);
        em.remove(findComment);
        return findComment.getId();
    }
}
