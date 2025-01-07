package com.gongkademy.controller;

import com.gongkademy.service.CourseService;
import com.gongkademy.service.dto.CourseDetailResponse;
import com.gongkademy.service.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    //강좌 수강 신청하기

    //강좌 수강 취소하기

    //강좌의 최근 수강 강의 조회

    //강좌 상세 조회
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@AuthenticationPrincipal PrincipalDetails principalDetails
            , @PathVariable Long courseId) {

        CourseDetailResponse courseDetailResponse = courseService.findCourseDetail(principalDetails.getMember().getId(), courseId);
        return ResponseEntity.ok(courseDetailResponse);
    }

    //강좌 목차 조회

    //강의 수강 기록 남기기

    //강의 상세 조회

    //강좌 자료 다운

    //강좌 홈 화면
}
