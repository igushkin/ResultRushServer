package com.example.ResultRush.service;

import com.example.ResultRush.configurations.Jwt.JwtIssuer;
import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.entity.Category;
import com.example.ResultRush.entity.Goal;
import com.example.ResultRush.entity.Milestone;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.model.LoginResponse;
import com.example.ResultRush.repository.CategoryRepository;
import com.example.ResultRush.repository.GoalRepository;
import com.example.ResultRush.repository.MilestoneRepository;
import com.example.ResultRush.repository.UserRepository;
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
                .user(user)
                .build();

        sampleCategory = categoryRepository.save(sampleCategory);

        var sampleGoal = Goal.builder()
                .title("Sample goal")
                .description("Here you can explain why this goal is important to you")
                .deadline(LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.UTC))
                .category(sampleCategory)
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