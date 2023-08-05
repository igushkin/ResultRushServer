package com.example.ResultRush.service;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.CategoryDto;
import com.example.ResultRush.repository.CategoryRepository;
import com.example.ResultRush.repository.GoalRepository;
import com.example.ResultRush.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    public CategoryDto getById(Integer id) {
        var category = categoryRepository.findByIdAndUserId(id, getPrincipalId()).get();
        var completedGoals = goalRepository.countAllByCategoryIdAndIsCompletedAndUserId(id, true, getPrincipalId());
        var uncompletedGoals = goalRepository.countAllByCategoryIdAndIsCompletedAndUserId(id, false, getPrincipalId());
        var totalGoals = completedGoals + uncompletedGoals;
        category.setCompletedGoals(completedGoals);
        category.setUncompletedGoals(uncompletedGoals);
        category.setTotalGoals(totalGoals);

        return CategoryDto.toCategoryDto(category);
    }

    public List<CategoryDto> getAll() {
        var categories = categoryRepository.findAllByUserIdOrderById(getPrincipalId())
                .stream()
                .map(x -> CategoryDto.toCategoryDto(x))
                .collect(Collectors.toList());

        for (var category : categories) {
            var completedGoals = goalRepository.countAllByCategoryIdAndIsCompletedAndUserId(category.getId(), true, getPrincipalId());
            var uncompletedGoals = goalRepository.countAllByCategoryIdAndIsCompletedAndUserId(category.getId(), false, getPrincipalId());
            var totalGoals = completedGoals + uncompletedGoals;
            category.setCompletedGoals(completedGoals);
            category.setUncompletedGoals(uncompletedGoals);
            category.setTotalGoals(totalGoals);
        }
        return categories;
    }


    public CategoryDto create(CategoryDto categoryDto) {
        var user = userRepository.findById(getPrincipalId()).get();
        var category = CategoryDto.toCategory(categoryDto);
        category.setUser(user);
        return CategoryDto.toCategoryDto(categoryRepository.save(category));
    }

    public CategoryDto update(CategoryDto categoryDto) {
        var categoryFromDb = categoryRepository.findByIdAndUserId(categoryDto.getId(), getPrincipalId()).get();

        categoryFromDb.setTitle(categoryDto.getTitle());
        categoryFromDb.setColor(categoryDto.getColor());

        return CategoryDto.toCategoryDto(categoryRepository.save(categoryFromDb));
    }

    @Transactional
    public void delete(Integer id) {

        var categoryFromDb = categoryRepository.findByIdAndUserId(id, getPrincipalId()).get();

        goalRepository.unlinkCategoriesByCategoryId(id, getPrincipalId());
        categoryRepository.delete(categoryFromDb);
    }

    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }
}