package com.simulacros.simulacro_1.api.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

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
public class EnrollmentReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The date cannot be in the past.")
    private LocalDate enrollment_date;

    @NotNull(message = "User id is required.")
    private Long id_user;

    @NotNull(message = "Course id is required.")
    private Long id_course;
}
