package com.gongkademy.repository;

import com.gongkademy.domain.View;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewRepositoryImpl implements ViewRepository{

    private final EntityManager em;

    @Override
    public Long save(View view) {
        em.persist(view);
        return view.getId();
    }

    @Override
    public List<View> findAllByBoardId(Long boardId) {
        return em.createQuery("SELECT v FROM View v WHERE v.board.id = :boardId",View.class)
                .setParameter("boardId",boardId)
                .getResultList();
    }
}
