package com.simulacros.simulacro_1.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseBasicResp {
    private Long id_course;

    private String course_name;

    private String description;
}
