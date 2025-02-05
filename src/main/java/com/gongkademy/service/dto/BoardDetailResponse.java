package com.gongkademy.service.dto;

import com.gongkademy.domain.board.BoardCategory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardDetailResponse {
    private Long boardId;
    private String title;
    private String body;
    private String date;
    private String nickname;
    private String courseTitle;
    private String lectureTitle;
    private Boolean isMine;
    private BoardCategory boardCategory;
    private List<CommentItemDto> commentList;
}
