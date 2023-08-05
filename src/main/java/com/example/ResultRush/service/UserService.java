package com.example.ResultRush.service;

import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<Usr> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}