package com.example.ResultRush.service;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.MilestoneDto;
import com.example.ResultRush.repository.GoalRepository;
import com.example.ResultRush.repository.MilestoneRepository;
import com.example.ResultRush.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public MilestoneDto create(MilestoneDto obj) {
        var milestone = MilestoneDto.toMilestone(obj);
        var goal = goalRepository.findByIdAndUserId(obj.getGoalId(), getPrincipalId()).get();
        var user = userRepository.findById(getPrincipalId()).get();

        if (goal.getUser().getId() != user.getId()) {
            throw new RuntimeException("Goal was not found");
        }

        milestone.setGoal(goal);
        return MilestoneDto.toMilestoneDto(milestoneRepository.save(milestone));
    }

    public MilestoneDto update(MilestoneDto obj) {
        var milestone = milestoneRepository.findById(obj.getId()).get();
        var goal = goalRepository.findById(obj.getGoalId()).get();
        var user = userRepository.findById(getPrincipalId()).get();

        if (goal.getUser().getId() != user.getId()) {
            throw new RuntimeException("Goal was not found");
        }

        milestone.setTitle(obj.getTitle());
        milestone.setIsCompleted(obj.getIsCompleted());
        milestone.setDeadline(obj.getDeadline());

        return MilestoneDto.toMilestoneDto(milestoneRepository.save(milestone));
    }

    public List<MilestoneDto> findAllByGoalId(Integer goalId) {
        return milestoneRepository.findAllByGoalIdAndGoal_UserIdOrderById(goalId, getPrincipalId())
                .stream()
                .map(milestone -> MilestoneDto.toMilestoneDto(milestone))
                .collect(Collectors.toList());
    }

    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }

    public MilestoneDto getById(Integer id) {
        return MilestoneDto.toMilestoneDto(milestoneRepository
                .findByIdAndGoal_UserId(id, getPrincipalId())
                .get());
    }

    public void delete(Integer id) {
        var milestone = milestoneRepository.findByIdAndGoal_UserId(id, getPrincipalId()).get();
        milestoneRepository.delete(milestone);
    }
}