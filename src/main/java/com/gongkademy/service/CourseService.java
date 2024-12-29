package com.gongkademy.service;

import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
import java.util.List;

public interface CourseService {

    Long registerCourse(Long memberId, Long courseId);

    Long dropCourse(Long memberId, Long courseId);

    CourseDetailResponse findCourseDetail(Long memberId, Long courseId);

    List<LectureDetailResponse> findLectureList(Long courseId);
}
