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
    public Long update(Member member) {
        Member findMember = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                .setParameter("email", member.getEmail())
                .getSingleResult();

        findMember.updateProfile(member);
       return findMember.getId();
    }
}
