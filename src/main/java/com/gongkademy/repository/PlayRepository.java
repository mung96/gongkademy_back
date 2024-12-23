package com.gongkademy.repository;

import com.gongkademy.domain.Play;

public interface PlayRepository {
    Long save(Play play);
    Play findByMemberIdAndCourseIdByModifiedTime(Long memberId, Long courseId);
    Play findByMemberIdAndLectureId(Long memberId, Long lectureId);
}
