package com.gongkademy.repository;

import com.gongkademy.domain.Lecture;

public interface LectureRepository {
    Lecture findLectureListByCourseId(Long courseId);
    Lecture findById(Long LectureId);
}
