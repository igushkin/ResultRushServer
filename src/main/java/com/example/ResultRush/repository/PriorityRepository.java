package com.example.ResultRush.repository;

import com.example.ResultRush.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Integer> {
    Optional<Priority> findByIdAndUserId(Integer id, Integer userId);

    List<Priority> findAllByUserIdOrderById(Integer userId);
}