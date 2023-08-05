package com.example.ResultRush.controller;

import com.example.ResultRush.dto.GoalDto;
import com.example.ResultRush.model.AllGoalsStat;
import com.example.ResultRush.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {
    private GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public GoalDto create(@RequestBody GoalDto obj) {
        return goalService.create(obj);
    }

    @PatchMapping
    public GoalDto update(@RequestBody GoalDto obj) {
        return goalService.update(obj);
    }

    @GetMapping("{id}")
    public GoalDto getById(@PathVariable Integer id) {
        return goalService.getById(id);
    }

    @GetMapping
    public List<GoalDto> getAll() {
        var res = goalService.getAll();
        return res;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        goalService.delete(id);
    }

    @GetMapping("/stat")
    public AllGoalsStat getStat() {
        return goalService.getStat();
    }
}