package com.gongkademy.repository;

import com.gongkademy.domain.course.Lecture;
import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    List<Lecture> findLecturesByCourseId(Long courseId);
    Optional<Lecture> findById(Long lectureId);

//    Optional<Lecture> findByCourseIdAndLectureOrder(Long courseId, Long lectureOrder);
    Long findLastLectureIdByCourseId(Long courseId);

    Long findPrevLectureIdByLectureId(Long lectureId);
    Long findNextLectureIdByLectureId(Long lectureId);


}
