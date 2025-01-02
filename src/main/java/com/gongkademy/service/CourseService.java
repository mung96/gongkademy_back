package com.gongkademy.service;

import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
import com.gongkademy.service.dto.LectureItemDto;
import com.gongkademy.service.dto.LectureListResponse;
import java.util.List;

public interface CourseService {

    Long registerCourse(Long memberId, Long courseId);

    Long dropCourse(Long memberId, Long courseId);

    CourseDetailResponse findCourseDetail(Long memberId, Long courseId);

    LectureListResponse findLectureList(Long memberId, Long courseId);

    LectureDetailResponse findLastLecture(Long memberId, Long courseId);

    Long saveLastPlayedTime(Long memberId, Long lectureId, int lastPlayedTime);

    LectureDetailResponse findLectureDetail(Long memberId, Long lectureId);
}
