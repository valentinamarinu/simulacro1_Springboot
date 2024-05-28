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
public class SubmissionResp {
    private Long id_submission;

    private String content;

    private LocalDate submission_date;

    private Double grade;

    /* Relación con tabla "User" */
    private UserBasicResp user;

    /* Relación con tabla "Assignment" */
    private AssignmentBasicResp assignment;
}
