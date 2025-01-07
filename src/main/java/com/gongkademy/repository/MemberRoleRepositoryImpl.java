package com.gongkademy.repository;

import com.gongkademy.domain.MemberRole;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRoleRepositoryImpl implements MemberRoleRepository {

    private final EntityManager em;

    @Override
    public List<MemberRole> findByMemberId(Long memberId) {
        return em.createQuery("SELECT mr FROM MemberRole mr WHERE mr.member.id = :memberId", MemberRole.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
