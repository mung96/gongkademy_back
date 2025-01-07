package com.gongkademy.repository;

import com.gongkademy.domain.MemberRole;
import java.util.List;

public interface MemberRoleRepository {
    List<MemberRole> findByMemberId(Long memberId);
}
