package com.gongkademy.service.dto;

import com.gongkademy.domain.course.PlayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LectureItemDto {
    private Long lectureId;
    private String title;
    private int runtime;
    private int lectureOrder;
    private PlayStatus playStatus;
}
