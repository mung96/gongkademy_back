package com.gongkademy.repository;

import com.gongkademy.domain.board.Board;
import com.gongkademy.domain.board.Question;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository{

    private final EntityManager em;

    @Override
    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    @Override
    public List<Board> findAllByMemberId(Long memberId) {
        return em.createQuery("SELECT b FROM Board b WHERE b.member.id = :memberId",Board.class)
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
        em.remove(board);
        return board.getId();
    }
}
