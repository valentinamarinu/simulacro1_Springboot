package com.simulacros.simulacro_1.api.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentBasicResp {
    private Long id_assignment;

    private String assignment_title;

    private String description;

    private LocalDate due_date;

    private LessonBasicResp lesson;
}
