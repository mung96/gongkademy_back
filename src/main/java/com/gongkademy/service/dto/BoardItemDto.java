package com.gongkademy.service.dto;

import com.gongkademy.domain.board.BoardCategory;
import java.time.LocalDateTime;
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
//    private LocalDateTime date;
    private int commentCount;
    private String boardCategory;  //스펙이 변경되니까 dto를 api마다 만드는게 좋구나.
    private String courseTitle;
    private String lectureTitle;
}
