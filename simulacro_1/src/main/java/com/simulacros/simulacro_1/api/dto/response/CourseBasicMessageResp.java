package com.simulacros.simulacro_1.api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseBasicMessageResp {
    private Long id_course;

    private String course_name;

    private String description;

    /* Relación con tabla "Lesson" */
    private List<LessonBasicResp> lessons;

    /* Relación con tabla intermedia "Enrollment" */
    private List<EnrollmentBasicResp> enrollments;
}
