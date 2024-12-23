package com.gongkademy.repository;

import com.gongkademy.domain.Play;

public interface PlayRepository {
    Long save(Play play);
    Play findByCourseIdByModifiedTime(Long courseId);
    Play findByLectureId(Long playId);
}
