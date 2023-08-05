package com.example.ResultRush.controller;

import com.example.ResultRush.dto.MilestoneDto;
import com.example.ResultRush.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/milestones")
public class MilestoneController {
    private MilestoneService milestoneService;

    @Autowired
    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @PostMapping
    public MilestoneDto create(@RequestBody MilestoneDto obj) {
        return milestoneService.create(obj);
    }

    @PatchMapping
    public MilestoneDto update(@RequestBody MilestoneDto obj) {
        return milestoneService.update(obj);
    }

    @GetMapping("{id}")
    public MilestoneDto getById(@PathVariable Integer id) {
        return milestoneService.getById(id);
    }

    @GetMapping("/goal/{goalId}")
    public List<MilestoneDto> getAll(@PathVariable Integer goalId) {
        return milestoneService.findAllByGoalId(goalId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        milestoneService.delete(id);
    }
}