package com.gongkademy.repository;

import com.gongkademy.domain.Play;
import java.util.Optional;

public interface PlayRepository {
    Long save(Play play);
    Optional<Play> findByMemberIdAndCourseIdByModifiedTime(Long memberId, Long courseId);
    Optional<Play> findByMemberIdAndLectureId(Long memberId, Long lectureId);
}
