package com.coinbet.simulator.controller;

import com.coinbet.simulator.dto.AuthDtos.RegisterRequest;
import com.coinbet.simulator.dto.AuthDtos.UserResponse;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request.username(), request.password());
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        AppUser user = userService.getByUsername(authentication.getName());
        return userService.toResponse(user);
    }
}
