package com.example.demo.adapters;

import com.example.demo.domain.User;
import com.example.demo.domain.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class InMemoryUserRepository implements UserRepository {
    private final Set<User> users = new HashSet<>();

    @Override
    public void register(User user) {
        this.users.add(user);
    }

    @Override
    public Set<User> all() {
        return this.users;
    }

    @Override
    public void clear() {
        this.users.clear();
    }
}
