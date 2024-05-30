package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.CourseReq;
import com.simulacros.simulacro_1.api.dto.response.CourseResp;

public interface ICourseService extends CrudService<CourseReq, CourseResp, Long> {
    public final String FIELD_BY_SORT = "course_name";
}
