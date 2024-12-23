package com.gongkademy.repository;

import com.gongkademy.domain.Regist;

public interface RegistRepository {
    Long save(Regist regist);

    Long deleteById(Long memberId, Long courseId);
}
