package com.example.ResultRush.controller;

import com.example.ResultRush.dto.PriorityDto;
import com.example.ResultRush.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/priorities")
public class PriorityController {

    private PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping
    public PriorityDto create(@RequestBody PriorityDto obj) {
        return priorityService.create(obj);
    }

    @PatchMapping
    public PriorityDto update(@RequestBody PriorityDto obj) {
        return priorityService.update(obj);
    }

    @GetMapping("{id}")
    public PriorityDto getById(@PathVariable Integer id) {
        return priorityService.getById(id);
    }

    @GetMapping
    public List<PriorityDto> getAll() {
        return priorityService.getAll();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        priorityService.delete(id);
    }
}