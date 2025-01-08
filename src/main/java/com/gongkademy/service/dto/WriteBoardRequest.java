package com.gongkademy.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WriteBoardRequest {
    @NotBlank
    @Min(0)
    @Max(100)
    private String title;

    @NotBlank
    @Min(0)
    @Max(10_000)
    private String body;

    @NotBlank
    private Long lectureId;
}
