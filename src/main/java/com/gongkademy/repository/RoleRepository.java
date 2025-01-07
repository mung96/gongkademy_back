package com.gongkademy.repository;

import com.gongkademy.domain.Role;
import com.gongkademy.domain.RoleType;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByRoleType(RoleType roleType);
}
