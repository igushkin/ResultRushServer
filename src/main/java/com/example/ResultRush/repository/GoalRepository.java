package com.example.ResultRush.repository;

import com.example.ResultRush.entity.Goal;
import com.example.ResultRush.entity.GoalStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    int countAllByCategoryIdAndIsCompletedAndUserId(Integer categoryId, Boolean status, Integer userId);

    @Query("SELECT new com.example.ResultRush.entity.GoalStat(count(*), g.isCompleted) "
            + "FROM Goal AS g where g.user.id = ?1 GROUP BY g.isCompleted")
    List<GoalStat> countTotalGoalsByStatus(Integer userId);

    @Modifying
    @Query("update Goal g set g.category = null where g.category.id = ?1 and g.user.id = ?2 ")
    void unlinkCategoriesByCategoryId(Integer id, Integer userId);

    @Modifying
    @Query("update Goal g set g.priority=null where g.priority.id = ?1 and g.user.id = ?2 ")
    void unlinkPrioritiesByPriorityId(Integer id, Integer userId);

    Optional<Goal> findByIdAndUserId(Integer id, Integer userId);

    List<Goal> findAllByUserId(Integer userId);
}