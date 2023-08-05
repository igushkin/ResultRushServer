package com.example.ResultRush.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @NotBlank
    private String title;
    @Basic
    private String description;
    @Basic(optional = false)
    private Boolean isCompleted = false;
    @Column
    private Instant deadline;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    private Priority priority;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer completedMilestones = 0;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer uncompletedMilestones = 0;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Usr user;
}