package com.example.ResultRush.dto;

import com.example.ResultRush.entity.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalDto {
    private Integer id;
    private String title;
    private String description;
    private Boolean isCompleted = false;
    private Instant deadline;
    private Integer categoryId;
    private Integer priorityId;
    private CategoryDto category;
    private PriorityDto priority;
    private Integer completedMilestones;
    private Integer uncompletedMilestones;
    private Integer userId;

    public static Goal toGoal(GoalDto goalDto) {
        var goal = new Goal();
        goal.setId(goalDto.getId());
        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setDeadline(goalDto.getDeadline());
        goal.setIsCompleted(goalDto.getIsCompleted());

        return goal;
    }

    public static GoalDto toGoalDto(Goal goal) {
        var goalDto = new GoalDto();
        goalDto.setId(goal.getId());
        goalDto.setTitle(goal.getTitle());
        goalDto.setDescription(goal.getDescription());
        goalDto.setDeadline(goal.getDeadline());
        goalDto.setIsCompleted(goal.getIsCompleted());
        if (goal.getCategory() != null) {
            goalDto.setCategoryId(goal.getCategory().getId());
            goalDto.setCategory(CategoryDto.toCategoryDto(goal.getCategory()));
        }
        if (goal.getPriority() != null) {
            goalDto.setPriorityId(goal.getPriority().getId());
            goalDto.setPriority(PriorityDto.toPriorityDto(goal.getPriority()));
        }
        goalDto.setCompletedMilestones(goal.getCompletedMilestones());
        goalDto.setUncompletedMilestones(goal.getUncompletedMilestones());
        goalDto.setUserId(goal.getUser().getId());
        return goalDto;
    }
}