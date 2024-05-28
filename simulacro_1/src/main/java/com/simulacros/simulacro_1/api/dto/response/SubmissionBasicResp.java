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
public class SubmissionBasicResp {
    private Long id_submission;

    private String content;

    private LocalDate submission_date;

    private Double grade;

    private AssignmentBasicResp assignment;
}
