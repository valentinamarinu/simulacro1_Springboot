package com.simulacros.simulacro_1.api.dto.response;

import com.simulacros.simulacro_1.util.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicResp {
    private Long id_user;

    private String username;

    private String password;

    private String email;

    private String full_name;

    private Role role;
}
