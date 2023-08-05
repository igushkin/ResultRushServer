package com.example.ResultRush.controller;

import com.example.ResultRush.model.LoginRequest;
import com.example.ResultRush.model.LoginResponse;
import com.example.ResultRush.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.attemptLogin(request.getLogin(), request.getPassword());
    }


    @PostMapping("/register")
    public boolean register(@RequestBody LoginRequest loginRequest) {
        return authService.register(loginRequest.getLogin(), loginRequest.getPassword());
    }

    @GetMapping("/get")
    public String login() {
        return "Hi!";
    }
}