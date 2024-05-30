package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.LessonReq;
import com.simulacros.simulacro_1.api.dto.response.LessonResp;

public interface ILessonService extends CrudService<LessonReq, LessonResp, Long> {
    public final String FIELD_BY_SORT = "lesson_title";
}
