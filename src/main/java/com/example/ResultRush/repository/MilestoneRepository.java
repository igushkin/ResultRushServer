package com.example.ResultRush.repository;

import com.example.ResultRush.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Integer> {

    List<Milestone> findAllByGoalIdAndGoal_UserIdOrderById(Integer goalId, Integer userId);

    Integer deleteAllByGoalIdAndGoal_UserId(Integer goalId, Integer userId);

    Optional<Milestone> findByIdAndGoal_UserId(Integer id, Integer userId);
}