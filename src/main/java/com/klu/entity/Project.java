package com.klu.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private String section;
    private String subject;
    private LocalDate deadline;
    private Integer groupSize;
    private String status; // Active / Archived etc.

    private String createdBy;
    private LocalDate createdAt;
}
