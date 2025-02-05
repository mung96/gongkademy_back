package com.gongkademy.repository;

import static com.gongkademy.domain.board.BoardCategory.QUESTION;
import static com.gongkademy.domain.board.BoardCategory.WORRY;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.BoardCategory;
import com.gongkademy.domain.board.BoardCriteria;
import com.gongkademy.domain.board.Question;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository{

    private final EntityManager em;

    @Override
    public List<Board> findAllByCategory(BoardCategory category, int page, BoardCriteria boardCriteria) {
        if(category == QUESTION){
            return em.createQuery("SELECT q FROM Question q WHERE q.isDeleted = false ORDER BY q.createdAt ASC",Board.class)
                    .setFirstResult((page-1)*20)
                    .setMaxResults(20)
                     .getResultList();
        }else if(category == WORRY){
            return em.createQuery("SELECT w FROM Worry w WHERE w.isDeleted = false ORDER BY w.createdAt ASC",Board.class)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }
        return List.of();
    }

    @Override
    public List<Board> findAllByCategoryAndMemberId(Long memberId, BoardCategory category, int page,
                                                    BoardCriteria boardCriteria) {
        if(category == QUESTION){
            return em.createQuery("SELECT q FROM Question q WHERE q.member.id = :memberId And q.isDeleted = false  ORDER BY q.createdAt ASC",Board.class)
                    .setParameter("memberId",memberId)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }else if(category == WORRY){
            return em.createQuery("SELECT w FROM Worry w WHERE w.member.id = :memberId And w.isDeleted = false  ORDER BY w.createdAt ASC",Board.class)
                     .setParameter("memberId",memberId)
                     .setFirstResult((page-1)*20)
                     .setMaxResults(20)
                     .getResultList();
        }
        return List.of();
    }

    @Override
    public Long countAllByCategoryAndMemberId(Long memberId, BoardCategory category) {
        Long totalBoard = 0L;
        if(category == QUESTION){
            totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.member.id = :memberId AND q.isDeleted = false",Long.class)
                           .setParameter("memberId",memberId)
                           .getSingleResult();
        }else if(category == WORRY){
            totalBoard = em.createQuery("SELECT count(w) FROM Worry w WHERE w.member.id = :memberId AND w.isDeleted = false",Long.class)
                           .setParameter("memberId",memberId)
                           .getSingleResult();
        }
        return ((totalBoard-1)/20)+1;
    }

    @Override
    public Long countAllByCategory(BoardCategory category) {
        Long totalBoard = 0L;
        if(category == QUESTION){
            totalBoard = em.createQuery("SELECT count(q) FROM Question q WHERE q.isDeleted = false",Long.class)
                      .getSingleResult();
        }else if(category == WORRY){
            totalBoard = em.createQuery("SELECT count(w) FROM Worry w WHERE w.isDeleted = false",Long.class)
                      .getSingleResult();
        }
        return ((totalBoard-1)/20)+1;
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        List<Board> result = em.createQuery("SELECT b FROM Board b WHERE b.id = :boardId AND b.isDeleted = false", Board.class)
                               .setParameter("boardId", boardId)
                               .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Override
    public List<Board> findAllByMemberId(Long memberId) {
        return em.createQuery("SELECT b FROM Board b WHERE b.member.id = :memberId AND b.isDeleted = false",Board.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    @Override
    public Long save(Board board) {
        em.persist(board);
        return board.getId();
    }

    @Override
    public Long update(Long boardId, Board board) {
        Board findBoard = em.find(Board.class,boardId);

        if(findBoard == null) throw new IllegalStateException("존재하지 않는 게시글입니다.");

        findBoard.changeTitle(board.getTitle());
        findBoard.changeBody(board.getBody());

        if(findBoard instanceof Question){
            ((Question) findBoard).changeLecture(((Question) board).getLecture());
        }
        return findBoard.getId();
    }

    @Override
    public Long delete(Board board) {
        board.delete();
        em.persist(board);
        return board.getId();
    }
}
