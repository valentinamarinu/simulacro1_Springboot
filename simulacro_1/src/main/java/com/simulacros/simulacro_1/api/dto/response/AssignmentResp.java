package com.simulacros.simulacro_1.api.dto.response;

import java.time.LocalDate;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResp {
    private Long id_assignment;

    private String assignment_title;

    private String description;

    private LocalDate due_date;

    /* Relación con tabla "Lesson" */
    private LessonBasicResp lesson;

    /* Relación con tabla "Submission" */
    private List<SubmissionBasicAssignmentResp> submissions;
}
