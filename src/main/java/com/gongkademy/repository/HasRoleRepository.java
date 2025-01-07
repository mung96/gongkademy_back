package com.gongkademy.repository;

import com.gongkademy.domain.HasRole;
import java.util.List;

public interface HasRoleRepository {
    List<HasRole> findByMemberId(Long memberId);
    Long save(HasRole hasRole);
}
