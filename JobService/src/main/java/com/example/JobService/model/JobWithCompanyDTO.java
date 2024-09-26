package com.example.JobService.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobWithCompanyDTO {
    private int postId;
    private String postProfile;
    private String postDesc;
    private Integer reqExperience; // Integer can be null, while int cannot.
    private String postTechStack;
    private CreateUserResponseModel company;
}
