package com.gongkademy.repository;

import com.gongkademy.domain.Lecture;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    List<Lecture> findLecturesByCourseId(Long courseId);
    Optional<Lecture> findById(Long lectureId);
}
