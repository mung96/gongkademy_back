package com.gongkademy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LectureDetailResponse {
    private String title;
    private Integer lastPlayedTime;
    private Long lastLectureId;
    private Long nextLectureId;
    private Long prevLectureId;
    private String url;
}
