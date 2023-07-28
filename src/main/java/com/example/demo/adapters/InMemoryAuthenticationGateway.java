package com.example.demo.adapters;

import com.example.demo.domain.User;
import com.example.demo.domain.gateways.AuthenticationGateway;

import java.util.Optional;

public class InMemoryAuthenticationGateway implements AuthenticationGateway {
    User currentUser;

    @Override
    public void authenticate(User u, String password) {
        if (u.getPassword().equals(password))
            currentUser = u;
        else
            currentUser = null;
    }

    @Override
    public Optional<User> currentUser() {
        return Optional.ofNullable(currentUser);
    }

    @Override
    public void clear() {
        currentUser = null;
    }
}
