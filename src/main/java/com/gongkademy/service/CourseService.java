package com.gongkademy.service;

public interface CourseService {

    Long registerCourse(Long memberId, Long courseId);

    Long dropCourse(Long memberId, Long courseId);
}
