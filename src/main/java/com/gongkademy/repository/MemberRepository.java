package com.gongkademy.repository;

import com.gongkademy.domain.Member;
import com.gongkademy.domain.Provider;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);

    Long save(Member member);
}
