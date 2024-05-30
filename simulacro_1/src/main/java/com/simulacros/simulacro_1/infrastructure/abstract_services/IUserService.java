package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.UserReq;
import com.simulacros.simulacro_1.api.dto.response.UserResp;

public interface IUserService extends CrudService<UserReq, UserResp, Long> {
    public final String FIELD_BY_SORT = "full_name";
}
