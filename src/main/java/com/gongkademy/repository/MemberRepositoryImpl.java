package com.gongkademy.repository;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.Provider;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    private final EntityManager em;

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        List<Member> memberList = em.createQuery("SELECT m FROM Member m WHERE m.nickname = :nickname", Member.class)
                                    .setParameter("nickname", nickname)
                                    .getResultList();

        return memberList.isEmpty() ? Optional.empty() : Optional.of(memberList.getFirst());
    }

    @Override
    public Optional<Member> findByProviderAndProviderId(Provider provider, String providerId) {
        List<Member> memberList = em.createQuery("SELECT m FROM Member m WHERE m.provider = :provider AND m.providerId = :providerId", Member.class)
                                    .setParameter("provider", provider)
                                    .setParameter("providerId", providerId)
                                    .getResultList();

        return memberList.isEmpty() ? Optional.empty() : Optional.of(memberList.getFirst());
    }

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }
}
