package com.example.ResultRush.service;

import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.dto.GoalDto;
import com.example.ResultRush.entity.Category;
import com.example.ResultRush.entity.Goal;
import com.example.ResultRush.entity.Priority;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.model.AllGoalsStat;
import com.example.ResultRush.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final CategoryRepository categoryRepository;
    private final PriorityRepository priorityRepository;
    private final MilestoneRepository milestoneRepository;
    private final UserRepository userRepository;

    public GoalDto create(GoalDto obj) {
        Category category = null;
        Priority priority = null;
        Usr user = userRepository.findById(getPrincipalId()).get();

        if (obj.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUserId(obj.getCategoryId(), getPrincipalId()).get();
        }
        if (obj.getPriorityId() != null) {
            priority = priorityRepository.findByIdAndUserId(obj.getPriorityId(), getPrincipalId()).get();
        }

        var goal = GoalDto.toGoal(obj);
        goal.setCategory(category);
        goal.setPriority(priority);
        goal.setUser(user);

        return GoalDto.toGoalDto(goalRepository.save(goal));
    }

    public GoalDto update(GoalDto obj) {
        Goal goal = goalRepository.findByIdAndUserId(obj.getId(), getPrincipalId()).get();
        Category category = null;
        Priority priority = null;

        if (obj.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUserId(obj.getCategoryId(), getPrincipalId()).get();
        }
        if (obj.getPriorityId() != null) {
            priority = priorityRepository.findByIdAndUserId(obj.getPriorityId(), getPrincipalId()).get();
        }

        goal.setTitle(obj.getTitle());
        goal.setDescription(obj.getDescription());
        goal.setDeadline(obj.getDeadline());
        goal.setCategory(category);
        goal.setPriority(priority);

        return GoalDto.toGoalDto(goalRepository.save(goal));
    }


    public GoalDto getById(Integer id) {
        return GoalDto.toGoalDto(goalRepository.findByIdAndUserId(id, getPrincipalId()).get());
    }


    public List<GoalDto> getAll() {
        return goalRepository.findAllByUserIdOrderById(getPrincipalId())
                .stream()
                .map(x -> GoalDto.toGoalDto(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer id) {
        var goal = goalRepository.findByIdAndUserId(id, getPrincipalId()).get();
        milestoneRepository.deleteAllByGoalIdAndGoal_UserId(id, getPrincipalId());
        goalRepository.delete(goal);
    }

    public AllGoalsStat getStat() {
        var res = goalRepository.countTotalGoalsByStatus(getPrincipalId());

        var completedCount = 0;
        var uncompletedCount = 0;

        for (var r : res) {
            if (r.getIsCompleted()) {
                completedCount = r.getAmount();
            } else {
                uncompletedCount = r.getAmount();
            }
        }

        var stat = new AllGoalsStat(completedCount, uncompletedCount);

        return stat;
    }

    private Integer getPrincipalId() {
        return ((UserPrincipal) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }
}