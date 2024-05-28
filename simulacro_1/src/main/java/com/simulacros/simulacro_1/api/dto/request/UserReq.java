package com.simulacros.simulacro_1.api.dto.request;

import com.simulacros.simulacro_1.util.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {
    @NotBlank(message = "The user's UserName is required.")
    private String username;

    @NotBlank(message = "The user's password is required.")
    private String password;

    @Email(message = "The email is not valid.")
    @Size(
        min = 5, 
        max = 100,
        message = "The email must be between 5 and 100 characters."
    )
    private String email;

    @Size(min = 10, max = 100, message = "The user's full name must be between 10 and 100 characters.")
    private String full_name;

    @NotNull(message = "The user role is required.")
    private Role role;
}
