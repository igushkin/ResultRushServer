package com.example.ResultRush.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllGoalsStat {
    private Integer completedGoals;
    private Integer uncompletedGoals;
    private Integer totalGoals;

    public AllGoalsStat(Integer completedGoals, Integer uncompletedGoals) {
        this.completedGoals = Math.toIntExact(completedGoals);
        this.uncompletedGoals = uncompletedGoals;
        this.totalGoals = this.completedGoals + this.uncompletedGoals;
    }
}