package com.example.ResultRush.controller;

import com.example.ResultRush.dto.CategoryDto;
import com.example.ResultRush.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path = "/categories")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto obj) {
        return categoryService.create(obj);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody CategoryDto obj) {
        return categoryService.update(obj);
    }

    @GetMapping({"{id}"})
    public CategoryDto getById(@PathVariable Integer id) {
        return categoryService.getById(id);
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.getAll();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}