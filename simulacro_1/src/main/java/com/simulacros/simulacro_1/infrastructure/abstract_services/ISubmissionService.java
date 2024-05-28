package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.SubmissionReq;
import com.simulacros.simulacro_1.api.dto.response.SubmissionResp;

public interface ISubmissionService extends CrudService<SubmissionReq, SubmissionResp, Long> {
    public final String FIELD_BY_SORT = "submission_date";
}
