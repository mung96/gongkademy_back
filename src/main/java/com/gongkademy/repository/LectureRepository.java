package com.gongkademy.repository;

import com.gongkademy.domain.Lecture;
import java.util.List;

public interface LectureRepository {
    List<Lecture> findLecturesByCourseId(Long courseId);
    Lecture findById(Long LectureId);
}
