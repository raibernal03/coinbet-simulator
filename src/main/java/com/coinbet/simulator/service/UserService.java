package com.coinbet.simulator.service;

import com.coinbet.simulator.dto.AuthDtos.UserResponse;
import com.coinbet.simulator.model.AppUser;
import com.coinbet.simulator.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(String username, String password) {
        String normalizedUsername = username.trim().toLowerCase();
        if (appUserRepository.existsByUsername(normalizedUsername)) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        AppUser user = appUserRepository.save(new AppUser(normalizedUsername, passwordEncoder.encode(password)));
        return toResponse(user);
    }

    public AppUser getByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getCoinBalance());
    }
}
