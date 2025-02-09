package com.gongkademy.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LectureListResponse {
    private Boolean isRegister;
    private Integer playTime;
    private List<LectureItemDto> lectureList;
}


