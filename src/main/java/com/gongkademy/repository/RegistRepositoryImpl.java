package com.gongkademy.repository;

import com.gongkademy.domain.Regist;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegistRepositoryImpl implements RegistRepository{

    private final EntityManager em;

    @Override
    public Long save(Regist regist) {
        //TODO: 이미 있는 예외면 여기서 터트려야하나?
        em.persist(regist);
        return regist.getId();
    }

    @Override
    public Long deleteById(Long memberId, Long courseId) {
        //TODO: 삭제 대상이 있어야 터트릴 수 있음.예외처리
        Regist regist = em.createQuery("SELECT r FROM Regist r WHERE r.member.id = :memberId AND r.course.id = :courseId",Regist.class)
                .setParameter("memberId", memberId)
                .setParameter("courseId", courseId)
                .getSingleResult();
        em.remove(regist);
        return regist.getId();
    }
}
