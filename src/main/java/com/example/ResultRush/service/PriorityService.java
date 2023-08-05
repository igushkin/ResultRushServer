package com.example.ResultRush.service;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.PriorityDto;
import com.example.ResultRush.repository.GoalRepository;
import com.example.ResultRush.repository.PriorityRepository;
import com.example.ResultRush.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Transactional
    public void delete(Integer id) {

        var priority = priorityRepository.findByIdAndUserId(id, getPrincipalId()).get();
        goalRepository.unlinkPrioritiesByPriorityId(id, getPrincipalId());
        priorityRepository.delete(priority);
    }

    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }

    public PriorityDto create(PriorityDto priorityDto) {
        var user = userRepository.findById(getPrincipalId()).get();
        var priority = PriorityDto.toPriority(priorityDto);
        priority.setUser(user);
        return PriorityDto.toPriorityDto(priorityRepository.save(priority));
    }

    public PriorityDto update(PriorityDto priorityDto) {
        var priority = PriorityDto.toPriority(priorityDto);
        var origianl = priorityRepository.findByIdAndUserId(priorityDto.getId(), getPrincipalId()).get();
        priority.setUser(origianl.getUser());
        return PriorityDto.toPriorityDto(priorityRepository.save(priority));
    }

    public PriorityDto getById(Integer id) {
        return PriorityDto.toPriorityDto(priorityRepository.findByIdAndUserId(id, getPrincipalId()).get());
    }

    public List<PriorityDto> getAll() {
        return priorityRepository.findAllByUserId(getPrincipalId())
                .stream()
                .map(x -> PriorityDto.toPriorityDto(x))
                .collect(Collectors.toList());
    }
}