package com.example.Blog.controller;

import com.example.Blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/addUser")
    public String addUser(@RequestParam("username") String username, @RequestParam("email") String email,
                          @RequestParam("name") String name, @RequestParam("password") String password) {
        userService.addUser(username, email, name, password);
        return "redirect:/posts";
    }

    @GetMapping("/login")
    public String showLogin(@RequestParam("username") String username, @RequestParam("email") String email,
                          @RequestParam("name") String name, @RequestParam("password") String password) {
        userService.addUser(username, email, name, password);
        return "redirect:/posts";
    }
}
