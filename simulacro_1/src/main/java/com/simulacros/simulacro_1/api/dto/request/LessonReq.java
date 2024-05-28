package com.simulacros.simulacro_1.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonReq {
    @NotBlank(message = "The title of the lesson is required.")
    @Size(max = 100, message = "The title of the lesson is a maximum of 100 characters.")
    private String lesson_title;

    private String content;

    @NotNull(message = "Course id is required.")
    private Long id_course;
}
