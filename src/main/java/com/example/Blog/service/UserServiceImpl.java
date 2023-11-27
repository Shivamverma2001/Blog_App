package com.example.Blog.service;

import com.example.Blog.model.User;
import com.example.Blog.repository.RoleRepository;
import com.example.Blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public User addUser(String username, String email, String name, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, name, encodedPassword);
        return null;
    }
}
