package com.example.demo.domain.repositories;

import com.example.demo.domain.User;

import java.util.Set;

public interface UserRepository {
    void register(User user);

    Set<User> all();

    void clear();
}
