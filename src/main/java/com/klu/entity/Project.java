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

    private String subject;
    private String title;
    private String description;
    private String section;
    private Integer groupSize;
    private String createdBy;
    private String status;
    private LocalDate deadline;
    private LocalDate createdAt;
}
