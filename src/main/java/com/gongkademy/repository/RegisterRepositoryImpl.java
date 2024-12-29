package com.gongkademy.repository;

import com.gongkademy.domain.Register;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegisterRepositoryImpl implements RegisterRepository {

    private final EntityManager em;

    @Override
    public Long save(Register register) {
        em.persist(register);
        return register.getId();
    }

    @Override
    public Optional<Register> findByMemberIdAndCourseId(Long memberId, Long courseId) {
        List<Register> registerList = em.createQuery("SELECT r FROM Register r WHERE r.member.id = :memberId AND r.course.id = :courseId",
                                                     Register.class)
                                        .setParameter("memberId", memberId)
                                        .setParameter("courseId", courseId)
                                        .getResultList();

        return registerList.isEmpty() ? Optional.empty() : Optional.of(registerList.getFirst());
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
