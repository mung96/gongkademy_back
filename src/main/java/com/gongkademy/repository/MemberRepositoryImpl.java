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
    public Member findById(Long memberId) {
        return em.find(Member.class,memberId);
    }

    @Override
    public Member findByNickname(String nickname) {
        return em.createQuery("SELECT m FROM Member m WHERE m.nickname = :nickname",Member.class)
                .setParameter("nickname",nickname)
                .getSingleResult();
    }

    @Override
    public Long update(String email, Member updateMember) {
        Member findMember = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();

        findMember.updateProfile(updateMember);
       return findMember.getId();
    }


}
