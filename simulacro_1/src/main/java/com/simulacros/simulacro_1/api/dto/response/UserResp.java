package com.simulacros.simulacro_1.api.dto.response;

import java.util.List;

import com.simulacros.simulacro_1.util.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {
    private Long id_user;

    private String username;

    private String password;

    private String email;

    private String full_name;

    private Role role;
    
    /* Relación con tabla "Course" */
    private List<CourseBasicResp> courses;

    /* Relación con tabla "Message" del remitente */
    private List<MessageSenderResp> messages_sender;

    /* Relación con tabla "Message" del destinatario */
    private List<MessageReceiverResp> messages_receiver;

    /* Relación con la tabla intermedia "Enrollment" */
    private List<EnrollmentBasicResp> enrollments;

    /* Relación con tabla intermedia "Submission" */
    private List<SubmissionBasicResp> submissions;
}
