package com.example.Blog.controller;

import com.example.Blog.model.Comment;
import com.example.Blog.model.Post;
import com.example.Blog.model.Tag;
import com.example.Blog.service.PostService;
import com.example.Blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public String getPost(Model model) {
//        List<Post> posts = postService.getPosts();
//        model.addAttribute("posts", posts);
//        return "show-posts";
        return findPaginatedPost(2, model);
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

    @GetMapping("/posts/search")
    public String searchPost(@RequestParam("keyword") String keyword, Model model) {
        List<Post> posts = postService.getPostsBySearch(keyword);
        model.addAttribute("posts", posts);
        return "show-posts";
    }

    @GetMapping("/posts/paginated")
    public String findPaginatedPost(@RequestParam("pageNumber") Integer pageNumber, Model model) {
        Page<Post> page = postService.findPaginated(pageNumber, 4);
        List<Post> posts = page.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalPosts",page.getTotalElements());

        return "show-posts";
    }

}
