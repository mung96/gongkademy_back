package com.gongkademy.service;

public interface CourseService {

    void registerCourse(Long courseId, Long memberId);

    void dropCourse(Long courseId, Long memberId);
}
