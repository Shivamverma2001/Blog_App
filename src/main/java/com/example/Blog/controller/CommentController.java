package com.example.Blog.controller;

import com.example.Blog.model.Comment;
import com.example.Blog.model.Post;
import com.example.Blog.service.CommentService;
import com.example.Blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/comments/{postId}")
    public String addComment(@PathVariable("postId") Integer postId,
                             @ModelAttribute("myComment") Comment myComment, Model model) {
        Post post = postService.getPost(postId);
        myComment.setPost(post);
        commentService.create(myComment);
        model.addAttribute("post", postService.getPost(postId));

        return "redirect:/posts/"+postId;
    }
}
