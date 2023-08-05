package com.example.ResultRush.repository;

import com.example.ResultRush.entity.Usr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usr, Integer> {

    Optional<Usr> findByUsername(String login);
}