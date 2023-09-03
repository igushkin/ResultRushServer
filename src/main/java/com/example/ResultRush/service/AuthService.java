package com.example.ResultRush.service;

import com.example.ResultRush.configurations.Jwt.JwtIssuer;
import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.entity.*;
import com.example.ResultRush.model.LoginResponse;
import com.example.ResultRush.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final GoalRepository goalRepository;
    private final MilestoneRepository milestoneRepository;
    private final PriorityRepository priorityRepository;

    public LoginResponse attemptLogin(String login, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var token = jwtIssuer.issue(JwtIssuer.Request.builder()
                .userId(principal.getId())
                .email(principal.getUsername())
                .roles(List.of())
                .build());

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    public boolean register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        }
        var user = new Usr();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);

        executeCreateUserScript(user);

        return true;
    }

    private void executeCreateUserScript(Usr user) {
        var sampleCategory = Category.builder()
                .title("Sample Category")
                .color("#83fd23")
                .completedGoals(0)
                .uncompletedGoals(0)
                .user(user)
                .build();

        sampleCategory = categoryRepository.save(sampleCategory);

        var samplePriority = Priority.builder()
                .title("Important")
                .user(user)
                .build();

        priorityRepository.save(samplePriority);

        var sampleGoal = Goal.builder()
                .title("Sample goal")
                .isCompleted(false)
                .completedMilestones(0)
                .uncompletedMilestones(0)
                .description("Here you can explain why this goal is important to you")
                .deadline(LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.UTC))
                .category(sampleCategory)
                .priority(samplePriority)
                .user(user)
                .build();

        sampleGoal = goalRepository.save(sampleGoal);

        var sampleMilestone = Milestone.builder()
                .title("Step 1")
                .isCompleted(true)
                .goal(sampleGoal)
                .build();

        milestoneRepository.save(sampleMilestone);

        sampleMilestone = Milestone.builder()
                .title("Step 2")
                .isCompleted(true)
                .goal(sampleGoal)
                .build();
    }
}