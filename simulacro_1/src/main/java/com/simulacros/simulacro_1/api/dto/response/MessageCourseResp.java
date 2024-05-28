package com.simulacros.simulacro_1.api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageCourseResp {
    private Long id_message;
    
    private String message_content;

    private LocalDateTime sent_date;

    /* Relación con tabla "Users" del remitente */
    private UserBasicResp sender;
    
    /* Relación con tabla "Users" del destinatario */
    private UserBasicResp receiver;
}
