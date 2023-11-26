package com.example.Blog.controller;

import com.example.Blog.model.Comment;
import com.example.Blog.model.Post;
import com.example.Blog.service.CommentService;
import com.example.Blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String addComment(Model model, @PathVariable("postId") Integer postId,
            @Valid @ModelAttribute("myComment") Comment myComment, BindingResult bindingResult) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", postService.getPost(postId));

        if (bindingResult.hasErrors()) {
            model.addAttribute("myComment", myComment);
            return "show-post";
        }

        myComment.setPost(post);
        commentService.create(myComment);

        return "redirect:/posts/"+postId;
    }

    @GetMapping("/comments/edit/{commentId}")
    public String updateComment(@PathVariable("commentId") Integer commentId, Model model) {
        Comment editingComment = commentService.findCommentById(commentId);
        model.addAttribute("editingComment", editingComment);

        return "edit-comment";
    }

    @PostMapping("/comments/edit/{commentId}")
    public String updateComment(Model model, @PathVariable("commentId") Integer commentId,
            @Valid @ModelAttribute("editingComment") Comment editingComment, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            editingComment.setId(commentId);
            model.addAttribute("editingComment", editingComment);
            return "edit-comment";
        }

        Post post = commentService.findCommentById(commentId).getPost();
        Comment updatedComment = commentService.update(commentId, editingComment);
        model.addAttribute("post", post);

        return "redirect:/posts/"+post.getId();
    }

    @GetMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId, Model model) {
        Post post = commentService.findCommentById(commentId).getPost();
        commentService.deleteById(commentId);

        return "redirect:/posts/"+post.getId();
    }
}
