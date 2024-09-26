package com.example.JobService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseModel {
    private int userId;
    private String username;
    private String email;
    private String companyId;
    private String companyName;
}
