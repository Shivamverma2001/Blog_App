package com.example.Blog.controller;

import com.example.Blog.model.Post;
import com.example.Blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String getPost(Model model) {
        List<Post> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        return "show-posts";
    }

    @PostMapping("/post")
    public String createPost(Model model) {
        model.addAttribute("post", new Post());
        return "post";
    }

    @PostMapping("/processPost")
    public String createPost(@Valid @ModelAttribute("post") Post post) {
        postService.createPost(post);
        return "redirect:/posts";
    }
}
