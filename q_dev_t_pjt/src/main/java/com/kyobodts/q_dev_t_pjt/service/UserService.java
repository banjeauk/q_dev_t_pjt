package com.kyobodts.q_dev_t_pjt.service;

import org.springframework.stereotype.Service;

import com.kyobodts.q_dev_t_pjt.entity.User;
import com.kyobodts.q_dev_t_pjt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public User register(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(new User(username, password, email));
    }
}