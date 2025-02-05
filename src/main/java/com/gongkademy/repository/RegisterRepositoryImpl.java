package com.gongkademy.repository;

import com.gongkademy.domain.course.Register;
import com.gongkademy.domain.course.RegisterStatus;
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
    public List<Register> findByMemberIdAndRegisterStatus(Long memberId, RegisterStatus registerStatus) {
        if(registerStatus == null){
            return em.createQuery("SELECT r FROM Register r WHERE r.member.id = :memberId", Register.class)
                     .setParameter("memberId", memberId)
                     .getResultList();
        }

        return  em.createQuery("SELECT r FROM Register r WHERE r.member.id = :memberId AND r.registerStatus = :registerStatus", Register.class)
                                        .setParameter("memberId", memberId)
                                        .setParameter("registerStatus", registerStatus)
                                        .getResultList();
    }

    @Override
    public Optional<Register> findByMemberIdAndCourseId(Long memberId, Long courseId) {
        List<Register> registerList = em.createQuery("SELECT r FROM Register r WHERE r.member.id = :memberId AND r.course.id = :courseId", Register.class)
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
