package com.gongkademy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EditBoardRequest {
    private String title;
    private String body;
    private Long lectureId;
}
