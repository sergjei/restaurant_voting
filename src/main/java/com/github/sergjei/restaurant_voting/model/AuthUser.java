package com.github.sergjei.restaurant_voting.model;


import jakarta.validation.constraints.NotNull;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public AuthUser(@NotNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "user=" + user +
                '}';
    }
}
