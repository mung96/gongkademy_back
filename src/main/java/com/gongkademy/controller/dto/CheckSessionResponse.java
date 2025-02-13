package com.gongkademy.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CheckSessionResponse {
    private Long memberId;
    private Boolean isLogin;
}
