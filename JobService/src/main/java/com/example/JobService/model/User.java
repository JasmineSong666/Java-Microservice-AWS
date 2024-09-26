package com.example.JobService.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String companyId;
    private String companyName;

}
