package com.gongkademy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentItemDto {
    private Long commentId;
    private String nickname;
    private String content;
    private String date;
    private Boolean isMine;
}
