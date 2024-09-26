package com.example.UserService.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Schema(hidden = true) // hide "id" field in API Doc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // let Hibernate to auto create the ID
    private int userId;

    @Column(nullable=false, length=50, unique=true)
    private String username;

    @Column(nullable=false, length=120, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true)
    private String companyId;

    @Column(nullable=false, unique=true)
    private String companyName;

//    @JsonIgnore
//    @OneToMany(mappedBy = "company")
//    private List<JobPost> jobs;



}
