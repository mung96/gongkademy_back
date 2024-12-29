package com.gongkademy.repository;

import com.gongkademy.domain.Register;
import java.util.Optional;

public interface RegisterRepository {
    Long save(Register register);

    Optional<Register> findByMemberIdAndCourseId(Long memberId, Long courseId);

    Long deleteById(Long memberId, Long courseId);
}
