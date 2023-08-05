package com.example.ResultRush.dto;

import com.example.ResultRush.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Integer id;
    private String title;
    private String color;
    private Integer completedGoals;
    private Integer uncompletedGoals;
    private Integer totalGoals;

    public static Category toCategory(CategoryDto categoryDto) {
        var category = new Category();
        category.setId(categoryDto.getId());
        category.setTitle(categoryDto.getTitle());
        category.setColor(categoryDto.getColor());
        category.setCompletedGoals(categoryDto.getCompletedGoals());
        category.setUncompletedGoals(categoryDto.getUncompletedGoals());
        category.setTotalGoals(categoryDto.getTotalGoals());

        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        var categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        categoryDto.setColor(category.getColor());
        categoryDto.setCompletedGoals(category.getCompletedGoals());
        categoryDto.setUncompletedGoals(category.getUncompletedGoals());
        categoryDto.setTotalGoals(category.getTotalGoals());

        return categoryDto;
    }
}