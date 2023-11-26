package com.example.Blog.service;

import com.example.Blog.model.Comment;
import com.example.Blog.model.Post;
import com.example.Blog.repository.CommentRepository;
import com.example.Blog.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void create(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment findCommentById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment update(Integer commentId, Comment comment) {
        Post post = this.findCommentById(commentId).getPost();
        comment.setId(commentId);
        comment.setPost(post);
        Comment updatedComment = commentRepository.save(comment);

        return updatedComment;

    }
}

