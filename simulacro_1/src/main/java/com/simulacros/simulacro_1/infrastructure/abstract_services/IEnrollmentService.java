package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.EnrollmentReq;
import com.simulacros.simulacro_1.api.dto.response.EnrollmentResp;

public interface IEnrollmentService extends CrudService<EnrollmentReq, EnrollmentResp, Long> {
    public final String FIELD_BY_SORT = "enrollment_date";
}
