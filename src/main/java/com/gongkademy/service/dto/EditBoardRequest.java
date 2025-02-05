package com.gongkademy.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EditBoardRequest {
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 1, max = 10_000)
    private String body;

    private Long lectureId;
    private Long courseId;
}
