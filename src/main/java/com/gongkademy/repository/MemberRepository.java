package com.gongkademy.repository;

import com.gongkademy.domain.Member;

public interface MemberRepository {
    Member findById(Long memberId);
    Member findByNickname(String nickname);
    Long update(String email, Member updateMember);
}
