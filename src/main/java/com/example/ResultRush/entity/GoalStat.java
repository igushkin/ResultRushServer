package com.example.ResultRush.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class GoalStat {
    private Integer amount;
    private Boolean isCompleted;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    Usr user;

    public GoalStat(Long amount, Boolean isCompleted) {
        this.amount = Math.toIntExact(amount);
        this.isCompleted = isCompleted;
    }
}