package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.AssignmentReq;
import com.simulacros.simulacro_1.api.dto.response.AssignmentResp;

public interface IAssignmentService extends CrudService<AssignmentReq, AssignmentResp, Long> {
    public final String FIELD_BY_SORT = "assignment_title";
}
