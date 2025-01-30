package com.gongkademy.repository;

import com.gongkademy.domain.course.Register;
import com.gongkademy.domain.course.RegisterStatus;
import java.util.List;
import java.util.Optional;

public interface RegisterRepository {
    Long save(Register register);

    List<Register> findByMemberIdAndRegisterStatus(Long memberId, RegisterStatus registerStatus);
    Optional<Register> findByMemberIdAndCourseId(Long memberId, Long courseId);

    Long delete(Register register);
}
