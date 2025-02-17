package com.gongkademy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CourseDetailResponse {
    private String title;
    private String thumbnail;
    /**
     * 강좌자료는 버튼 눌렀을때 반환하는걸로 변경
     */
//    private String courseNote;
    private int courseTime;
    private Boolean isRegister;
}
