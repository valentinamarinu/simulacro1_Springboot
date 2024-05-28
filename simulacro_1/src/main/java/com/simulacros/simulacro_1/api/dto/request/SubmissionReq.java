package com.simulacros.simulacro_1.api.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionReq {
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The date cannot be in the past.")
    private LocalDate submission_date;

    @DecimalMax(value = "5.0")
    @DecimalMin(value = "0.0")
    private Double grade;

    @NotNull(message = "User id is required.")
    private Long id_user;

    @NotNull(message = "Assignment id is required.")
    private Long id_assignment;
}
