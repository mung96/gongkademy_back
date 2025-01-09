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

        return registerList.isEmpty() ? Optional.empty() : Optional.of(registerList.get(0));
    }

    @Override
    public Long delete(Register register) {
        em.remove(register);
        return register.getId();
    }
}
