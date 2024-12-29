package com.gongkademy.repository;

import com.gongkademy.domain.Register;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegistRepositoryImpl implements RegistRepository{

    private final EntityManager em;

    @Override
    public Long save(Register register) {
        //TODO: 이미 있는 예외면 여기서 터트려야하나?
        em.persist(register);
        return register.getId();
    }

    @Override
    public Long deleteById(Long memberId, Long courseId) {
        //TODO: 삭제 대상이 있어야 터트릴 수 있음.예외처리
        Register register = em.createQuery("SELECT r FROM Register r WHERE r.member.id = :memberId AND r.course.id = :courseId",
                                         Register.class)
                            .setParameter("memberId", memberId)
                            .setParameter("courseId", courseId)
                            .getSingleResult();
        em.remove(register);
        return register.getId();
    }
}
