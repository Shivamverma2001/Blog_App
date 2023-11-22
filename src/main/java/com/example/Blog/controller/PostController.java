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
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/posts/edit/{postId}")
    public String updatePost(@PathVariable("postId") Integer postId, Model model) {
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

    @GetMapping("/posts")
    public String getPost(Model model) {
        List<Post> posts = postService.getPosts();
        model.addAttribute("posts", posts);
        return "show-posts";
    }

    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Integer postId, Model model) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("myComment", new Comment());
        return "show-post";
    }

    @PostMapping("/post")
    public String createPost(Model model) {
        model.addAttribute("post", new Post());
        return "post";
    }

    @PostMapping("/processPost")
    public String createPost(
            @Valid @ModelAttribute("post") Post post,
            BindingResult bindingResult,
            @RequestParam("tagNames") String tagNames,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);

            return "post";
        }

        for (String tagName: tagNames.split(",")) {
            Tag tag = tagService.findByName(tagName);

            if (tag != null) {
                post.addTag(tag);
            } else {
                Tag newTag = tagService.createTag(new Tag(tagName));
                post.addTag(newTag);
            }
        }

        postService.createPost(post);
        return "redirect:/posts";
    }


    @PostMapping("posts/edit/{postId}")
    public String updatePost(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult,
            @RequestParam("tagNames") String tagNames, Model model, @PathVariable("postId") Integer postId) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);

            return "edit-post";
        }

        post.getTags().clear();

        for (String tagName: tagNames.split(",")) {
            Tag tag = tagService.findByName(tagName);

            if (tag != null) {
                post.addTag(tag);
            } else {
                Tag newTag = tagService.createTag(new Tag(tagName));
                post.addTag(newTag);
            }
        }

        post.setId(postId);
        postService.updatePost(post);

        return "redirect:/posts/"+postId;
    }
}
