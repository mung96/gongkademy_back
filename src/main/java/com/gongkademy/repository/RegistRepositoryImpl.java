package com.gongkademy.repository;

import com.gongkademy.domain.Regist;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegistRepositoryImpl implements RegistRepository{

    private final EntityManager em;

    @Override
    public Long save(Regist regist) {
        em.persist(regist);
        return regist.getId();
    }

    @Override
    public Long deleteById(Long registId) {
        return 0L;
    }
}
