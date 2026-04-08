package com.klu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role; // admin or student

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_enrollments", joinColumns = @JoinColumn(name = "user_id"))
    private List<Enrollment> enrollments = new ArrayList<>();
}
