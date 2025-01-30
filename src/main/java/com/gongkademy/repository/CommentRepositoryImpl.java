package com.gongkademy.repository;

import com.gongkademy.domain.board.Comment;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final EntityManager em;

    @Override
    public Optional<Comment> findById(Long commentId) {
        //TODO: test코드 작성해야함
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

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
    public Long delete(Comment comment) {
        em.remove(comment);
        return comment.getId();
    }
}
