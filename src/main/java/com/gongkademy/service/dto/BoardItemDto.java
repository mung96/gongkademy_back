package com.gongkademy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardItemDto {
    private String title;
    private String body;
    private String date;
    private int commentCount;
    private String courseTitle;
}
