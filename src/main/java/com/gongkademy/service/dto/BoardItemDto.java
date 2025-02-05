package com.gongkademy.service.dto;

import com.gongkademy.domain.board.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardItemDto {
    private Long boardId;
    private String title;
    private String body;
    private String date;
    private int commentCount;
    private BoardCategory boardCategory;
    private String courseTitle;
    private String lectureTitle;
}
