package com.simulacros.simulacro_1.api.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentReq {
    @NotBlank(message = "The assignment's title is required.")
    @Size(max = 100, message = "The assignment's title maximum length is 100 characters.")
    private String assignment_title;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The date cannot be in the past.")
    private LocalDate due_date;

    @NotNull(message = "Lesson id is required.")
    private Long id_lesson;
}
