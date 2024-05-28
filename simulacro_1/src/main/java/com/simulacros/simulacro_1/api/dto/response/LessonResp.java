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
public class LessonResp {
    private Long id_lesson;

    private String lesson_title;

    private String content;

    /* Relación con tabla "Assignment" */
    private List<AssignmentBasicLessonResp> assignments;

    /* Relación con tabla "Course" */
    private CourseBasicResp course;
}
