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
    private String courseNote;
    private int courseTime;
    private Boolean isRegister;
}
