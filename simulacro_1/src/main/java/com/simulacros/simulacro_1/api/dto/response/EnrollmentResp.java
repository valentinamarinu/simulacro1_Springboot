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
public class EnrollmentResp {
    private Long id_enrollment;

    private LocalDate enrollment_date;

    /* Relación con tabla "User" */
    private UserBasicResp user;

    /* Relación con tabla "Course" */
    private CourseBasicResp course;
}
