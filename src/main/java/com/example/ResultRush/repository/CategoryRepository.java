package com.example.ResultRush.repository;

import com.example.ResultRush.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Optional<Category> findByIdAndUserId(Integer id, Integer userId);

    List<Category> findAllByUserIdOrderById(Integer userId);

}