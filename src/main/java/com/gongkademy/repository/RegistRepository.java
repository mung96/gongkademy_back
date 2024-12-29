package com.gongkademy.repository;

import com.gongkademy.domain.Register;

public interface RegistRepository {
    Long save(Register register);

    Long deleteById(Long memberId, Long courseId);
}
