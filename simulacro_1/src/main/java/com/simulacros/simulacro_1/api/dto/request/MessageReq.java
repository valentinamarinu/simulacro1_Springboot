package com.simulacros.simulacro_1.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageReq {
    private String message_content;
    
    @NotNull(message = "User sender id is required.")
    private Long id_sender;

    @NotNull(message = "User receiver id is required.")
    private Long id_receiver;

    @NotNull(message = "Course id is required.")
    private Long id_course;
}
