package com.gongkademy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DownloadCourseNoteResponse {
    private String courseNoteUrl;
}
