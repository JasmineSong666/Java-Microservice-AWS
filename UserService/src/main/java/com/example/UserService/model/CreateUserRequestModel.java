package com.example.UserService.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestModel {

    @NotNull(message="Username cannot be null")
    @Size(min=3, message="Username must be more than 2 characters")
    private String username;
    @NotNull(message="Email cannot be null")
    @Email
    private String email;
    @NotNull(message="Password cannot be null")
    @Size(min=4, max=10, message="Password must be equal or greater than 4 characters and less than 10 characters")
    private String password;
    @NotNull(message="Company name cannot be null")
    private String companyName;

}

