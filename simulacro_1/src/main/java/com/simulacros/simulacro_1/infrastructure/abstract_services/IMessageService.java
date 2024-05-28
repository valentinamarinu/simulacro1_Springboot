package com.simulacros.simulacro_1.infrastructure.abstract_services;

import com.simulacros.simulacro_1.api.dto.request.MessageReq;
import com.simulacros.simulacro_1.api.dto.response.MessageResp;

public interface IMessageService extends CrudService<MessageReq, MessageResp, Long> {
    
}
