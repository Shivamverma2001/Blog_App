package com.example.Blog.restcontroller;

import com.example.Blog.model.Post;
import com.example.Blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostRestController {
    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/rest/posts")
    public ResponseEntity<List<Post>> getPosts(Model model,
                                         @RequestParam("pageNumber") Integer pageNumber,
                                         @RequestParam("field") String field,
                                         @RequestParam("direction") String direction,
                                         @RequestParam("authors") String authors,
                                         @RequestParam("tags") String tags,
                                         @RequestParam(value = "search") String search) {
        Page<Post> page = postService.getPosts(model, pageNumber, field, direction, authors, tags, search);
        List<Post> posts = page.getContent();

        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

    @GetMapping("/rest/posts/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable("postId") Integer postId) {
        Post post = postService.getPost(postId);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/rest/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam("tags") String tags) {
        Post newPost = postService.savePost(post, tags);

       return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @PutMapping("/rest/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") Integer postId,
                                             @RequestBody Post post,
                                             @RequestParam("tags") String tags,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Post currentPost = postService.getPost(postId);
        if (currentPost == null) {

            return new ResponseEntity<>("Post not exist.", HttpStatus.NOT_FOUND);

        } else if(userDetails.getUsername().equals(currentPost.getAuthor()) ||
                userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
            post.setId(postId);
            Post newPost = postService.savePost(post, tags);

            return new ResponseEntity<>("Post updated successfully.", HttpStatus.OK);

        } else {

            return new ResponseEntity<>("You are neither admin nor author of this post.", HttpStatus.UNAUTHORIZED);
        }

    }

    @DeleteMapping("/rest/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Integer postId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Post currentPost = postService.getPost(postId);
        if (currentPost == null) {

            return new ResponseEntity<>("Post not exist.", HttpStatus.NOT_FOUND);

        } else if (userDetails.getUsername().equals(currentPost.getAuthor()) ||
                userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
            postService.deletePostById(postId);

            return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);

        } else {

            return new ResponseEntity<>("You are neither admin nor author of this post.",
                    HttpStatus.UNAUTHORIZED);
        }
    }

}
