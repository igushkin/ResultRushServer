package com.example.ResultRush.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @NotBlank
    private String title;
    @Basic(optional = false)
    private String color;
    @Basic(optional = false)
    private Integer completedGoals = 0;
    @Basic(optional = false)
    private Integer uncompletedGoals = 0;
    @Basic(optional = false)
    private Integer totalGoals = 0;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Usr user;
}