package com.example.Blog.service;

import com.example.Blog.model.User;

public interface UserService {
    public User addUser(String username, String email, String name, String password);
}
