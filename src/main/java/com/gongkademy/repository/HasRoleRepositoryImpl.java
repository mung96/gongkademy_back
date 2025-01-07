package com.gongkademy.repository;

import com.gongkademy.domain.HasRole;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HasRoleRepositoryImpl implements HasRoleRepository {

    private final EntityManager em;

    @Override
    public List<HasRole> findByMemberId(Long memberId) {
        return em.createQuery("SELECT mr FROM HasRole mr WHERE mr.member.id = :memberId", HasRole.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public Long save(HasRole hasRole) {
        em.persist(hasRole);
        return hasRole.getId();
    }
}
