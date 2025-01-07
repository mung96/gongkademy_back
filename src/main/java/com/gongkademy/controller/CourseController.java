package com.gongkademy.controller;

import com.gongkademy.service.CourseService;
import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.LectureDetailResponse;
import com.gongkademy.service.dto.LectureListResponse;
import com.gongkademy.service.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    //강좌 수강 신청하기
    @PostMapping("/{courseId}/register")
    public ResponseEntity<LectureDetailResponse> registerCourse (@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long courseId) {

        courseService.registerCourse(principalDetails.getMember().getId(), courseId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //강좌 수강 취소하기

    //강좌의 최근 수강 강의 조회
    @GetMapping("/{courseId}/recent")
    public ResponseEntity<LectureDetailResponse> getLastLecture (@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long courseId) {

        LectureDetailResponse lastLecture = courseService.findLastLecture(principalDetails.getMember().getId(), courseId);
        return ResponseEntity.ok(lastLecture);
    }

    //강좌 상세 조회
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long courseId) {

        CourseDetailResponse courseDetailResponse = courseService.findCourseDetail(principalDetails.getMember().getId(), courseId);
        return ResponseEntity.ok(courseDetailResponse);
    }

    //강좌 목차 조회
    @GetMapping("/{courseId}/lectures")
    public ResponseEntity<LectureListResponse> getLecture(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long courseId) {

        LectureListResponse lectureList = courseService.findLectureList(principalDetails.getMember().getId(), courseId);
        return ResponseEntity.ok(lectureList);
    }

    //강의 수강 기록 남기기

    //강의 상세 조회
    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<LectureDetailResponse> getLectureDetail(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long lectureId) {

        LectureDetailResponse lectureDetail = courseService.findLectureDetail(principalDetails.getMember().getId(), lectureId);
        return ResponseEntity.ok(lectureDetail);
    }

    //TODO: 강좌 자료 다운

    //TODO: 강좌 홈 화면
}
