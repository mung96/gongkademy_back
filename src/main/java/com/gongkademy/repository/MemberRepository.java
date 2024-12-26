package com.gongkademy.repository;

import com.gongkademy.domain.Member;

public interface MemberRepository {
    Long update(String email, Member updateMember);
}
