package com.gongkademy.repository;

import com.gongkademy.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    private final EntityManager em;

    @Override
    public Long update(String email, Member updateMember) {
        Member findMember = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();

        findMember.updateProfile(updateMember);
       return findMember.getId();
    }
}
