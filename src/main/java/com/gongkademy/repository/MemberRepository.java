package com.gongkademy.repository;

import com.gongkademy.domain.Member;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByNickname(String nickname);
    Long save(Member member);
}
