package com.example.Blog.restcontroller;

import com.example.Blog.model.Comment;
import com.example.Blog.model.Post;
import com.example.Blog.service.CommentService;
import com.example.Blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentRestController {
    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CommentRestController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping("/rest/comments/{postId}")
    public ResponseEntity<List<Comment>> getCommentOfPost(@PathVariable("postId") Integer postId) {
        List<Comment> comments = commentService.getCommentOfPost(postId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/rest/{postId}/comments")
    public ResponseEntity<String> createComment(@RequestBody Comment comment, @PathVariable("postId") Integer postId) {
        Post post = postService.getPost(postId);
        comment.setPost(post);
        commentService.create(comment);

        return new ResponseEntity<>("Comment added successfully", HttpStatus.CREATED);
    }

     @PutMapping("/rest/comments/{commentId}")
     public ResponseEntity<String> updateComment(@RequestBody Comment comment,
                                                 @PathVariable("commentId") Integer commentId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
         Post post = commentService.findCommentById(commentId).getPost();
         Comment oldComment = commentService.findCommentById(commentId);

         if (oldComment == null) {

             return new ResponseEntity<>("Comment not exist", HttpStatus.NOT_FOUND);

         } else if(userDetails.getUsername().equals(post.getAuthor()) ||
                 userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
             comment.setPost(post);
             comment.setId(commentId);
             commentService.update(commentId, comment);

             return new ResponseEntity<>("Comment updated successfully", HttpStatus.OK);

         } else {

             return new ResponseEntity<>("You are neither admin nor author of this post.",
                     HttpStatus.UNAUTHORIZED);
         }
     }

    @DeleteMapping("/rest/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Integer commentId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {

            return new ResponseEntity<>("Comment not exist", HttpStatus.NOT_FOUND);

        } else if(userDetails.getUsername().equals(comment.getPost().getAuthor()) ||
                userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
            commentService.deleteById(commentId);

            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);

        } else {

            return new ResponseEntity<>("You are neither admin nor author of this post.",
                    HttpStatus.UNAUTHORIZED);
        }
    }

}
