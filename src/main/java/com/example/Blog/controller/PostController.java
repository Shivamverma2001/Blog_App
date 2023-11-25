package com.example.Blog.controller;

import com.example.Blog.model.Comment;
import com.example.Blog.model.Post;
import com.example.Blog.model.Tag;
import com.example.Blog.service.PostService;
import com.example.Blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String getPost(
            Model model,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "field", required = false) String field,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "authors", required = false) String authors,
            @RequestParam(value = "tags", required = false) String tags) {

        if (authors == null && tags == null) {
            postService.findPaginated(model, pageNumber, field, direction);
        } else {
            postService.getPaginatedFilteredPosts(model, pageNumber, field, direction, authors, tags);
        }

        return "show-posts";
    }

    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Integer postId, Model model) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("myComment", new Comment());
        return "show-post";
    }

    @PostMapping("/posts")
    public String createPost(Model model) {
        model.addAttribute("post", new Post());
        return "post";
    }

    @PostMapping("/posts/save")
    public String createPost(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult,
            @RequestParam("tagNames") String tagNames, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post";
        }

        postService.savePost(post, tagNames);
        return "redirect:/posts";
    }

    @GetMapping("/posts/edit/{postId}")
    public String getUpdatePostForm(@PathVariable("postId") Integer postId, Model model) {
        Post postToBeEdited = postService.getPost(postId);
        StringBuilder tagValuesBuilder = new StringBuilder();

        for(Tag tag:postToBeEdited.getTags()) {
            tagValuesBuilder.append(tag.getName());
            tagValuesBuilder.append(",");
        }

        if(!tagValuesBuilder.isEmpty()){
            tagValuesBuilder.deleteCharAt(tagValuesBuilder.length() - 1);
        }

        model.addAttribute("tagValues", tagValuesBuilder.toString());
        model.addAttribute("post", postToBeEdited);
        return "edit-post";
    }

    @PostMapping("/posts/edit/{postId}")
    public String updatePost(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult,
            @RequestParam("tagNames") String tagNames, Model model, @PathVariable("postId") Integer postId) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "edit-post";
        }

        post.setId(postId);
        postService.savePost(post, tagNames);

        return "redirect:/posts/"+postId;
    }

    @GetMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePostById(postId);
        return "redirect:/posts";
    }

}
