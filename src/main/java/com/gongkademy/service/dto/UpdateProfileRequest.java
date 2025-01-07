package com.gongkademy.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(min=2, max=20,message = "닉네임은 2자 이상 20자 이하입니다.")
    @Pattern(regexp="^[a-zA-Z가-힣]*$", message="닉네임은 오직 문자만 포함할 수 있습니다.")
    private String nickname;
}
