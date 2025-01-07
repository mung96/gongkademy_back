package com.gongkademy.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SavePlayRequest {

    @NotBlank
    @Min(0)
    private int lastPlayedTime;
}
