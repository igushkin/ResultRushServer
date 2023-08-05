package com.example.ResultRush.dto;

import com.example.ResultRush.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriorityDto {
    private Integer id;
    private String title;

    public static Priority toPriority(PriorityDto priorityDto) {
        var priority = new Priority();
        priority.setId(priorityDto.getId());
        priority.setTitle(priorityDto.getTitle());

        return priority;
    }

    public static PriorityDto toPriorityDto(Priority priority) {
        var priorityDto = new PriorityDto();
        priorityDto.setId(priority.getId());
        priorityDto.setTitle(priority.getTitle());

        return priorityDto;
    }
}