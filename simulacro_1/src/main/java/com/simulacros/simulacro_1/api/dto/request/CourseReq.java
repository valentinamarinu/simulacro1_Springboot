package com.simulacros.simulacro_1.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseReq {
    @NotBlank(message = "Course name is required.")
    private String course_name;

    private String description;

    @NotNull(message = "User id is required.")
    private Long id_user;
}
