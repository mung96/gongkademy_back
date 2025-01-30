package com.gongkademy.repository;

import com.gongkademy.domain.Role;
import com.gongkademy.domain.RoleType;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository{

    private final EntityManager em;

    @Override
    public Optional<Role> findByRoleType(RoleType roleType) {
        List<Role> roleList = em.createQuery("SELECT r FROM Role r WHERE r.roleType = :roleType", Role.class)
                               .setParameter("roleType", roleType)
                               .getResultList();

        return roleList.isEmpty() ? Optional.empty() : Optional.of(roleList.get(0));
    }

}
