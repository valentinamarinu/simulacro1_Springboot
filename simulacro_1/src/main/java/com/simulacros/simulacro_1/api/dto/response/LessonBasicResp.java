package com.simulacros.simulacro_1.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonBasicResp {
    private Long id_lesson;

    private String lesson_title;

    private String content;
}
