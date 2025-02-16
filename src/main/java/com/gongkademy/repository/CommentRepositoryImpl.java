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
        return Optional.ofNullable(em.createQuery("SELECT c FROM Comment c WHERE c.id = :commentId AND c.isDeleted = false",Comment.class)
                                     .setParameter("commentId",commentId)
                                     .getSingleResult());
    }

//    @Override
//    public Optional<Comment> findById(Long commentId) {
//        return Optional.ofNullable(em.createQuery("SELECT c FROM Comment c JOIN FETCH c.board WHERE c.id = :commentId AND c.isDeleted = false", Comment.class)
//                                     .setParameter("commentId", commentId)
//                                     .getSingleResult());
//    }

    @Override
    public List<Comment> findByBoardId(Long boardId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.isDeleted = false ORDER BY c.createdAt DESC",Comment.class)
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
//        em.remove(comment);
        comment.delete();
        em.persist(comment);
        return comment.getId();
    }
}
