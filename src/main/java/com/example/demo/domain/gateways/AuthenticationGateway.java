package com.example.demo.domain.gateways;

import com.example.demo.domain.User;

import java.util.Optional;

public interface AuthenticationGateway {
    void authenticate(User u, String password);

    Optional<User> currentUser();

    void clear();
}
