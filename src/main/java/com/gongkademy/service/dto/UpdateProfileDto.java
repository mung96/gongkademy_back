package com.gongkademy.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateProfileDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, max=20, message = "닉네임은 2자 이상 20자 이하입니다.")
    @Pattern(regexp="^[a-zA-Z가-힣]*$", message="닉네임은 오직 문자만 포함할 수 있습니다.")
    private String nickname;
}
