package com.example.ResultRush.IntegrationalTests.util;

import com.example.ResultRush.dto.MilestoneDto;
import com.example.ResultRush.entity.*;
import jakarta.persistence.criteria.CriteriaBuilder;

public class InstanceCreator {

    private static Integer counter = 0;

    public static Usr createUser() {
        var user = Usr.builder()
                .username("username" + counter)
                .password("password" + counter)
                .build();
        counter++;
        return user;
    }

    public static Category createCategory() {
        var category = Category.builder()
                .title("category" + counter)
                .color("#fff" + counter)
                .completedGoals(0)
                .uncompletedGoals(0)
                .totalGoals(0)
                .build();
        counter++;
        return category;
    }

    public static Priority createPriority() {
        var priority = Priority.builder()
                .title("priority")
                .build();
        counter++;
        return priority;
    }

    public static Goal createGoal() {
        var goal = Goal.builder()
                .title("title" + counter)
                .description("desc" + counter)
                .completedMilestones(0)
                .uncompletedMilestones(0)

                .isCompleted(false)
                .build();
        counter++;
        return goal;
    }

    public static Milestone createMilestone() {
        var milestone = Milestone.builder()
                .title("milestone title" + counter)
                .isCompleted(false)
                .build();
        counter++;
        return milestone;
    }
}