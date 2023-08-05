package com.example.ResultRush.dto;

import com.example.ResultRush.entity.Milestone;
import com.example.ResultRush.entity.Priority;
import com.example.ResultRush.service.MilestoneService;
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
public class MilestoneDto {

    private Integer id;
    private String title;
    private Instant deadline;
    private Boolean isCompleted;
    private Integer goalId;

    public static Milestone toMilestone(MilestoneDto milestoneDto) {
        var milestone = new Milestone();
        milestone.setId(milestoneDto.getId());
        milestone.setTitle(milestoneDto.getTitle());
        milestone.setDeadline(milestoneDto.getDeadline());
        milestone.setIsCompleted(milestoneDto.getIsCompleted());
        return milestone;
    }

    public static MilestoneDto toMilestoneDto(Milestone milestone) {
        var milestoneDto = new MilestoneDto();
        milestoneDto.setId(milestone.getId());
        milestoneDto.setTitle(milestone.getTitle());
        milestoneDto.setDeadline(milestone.getDeadline());
        milestoneDto.setIsCompleted(milestone.getIsCompleted());
        milestoneDto.setGoalId(milestone.getGoal() == null ? null : milestone.getGoal().getId());
        return milestoneDto;
    }
}