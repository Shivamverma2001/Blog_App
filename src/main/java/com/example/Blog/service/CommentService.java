package com.example.Blog.service;

import com.example.Blog.model.Comment;

import java.util.List;

public interface CommentService {
    public void create(Comment comment);

    public Comment findCommentById(Integer id);

    public void deleteById(Integer id);

    public Comment update(Integer commentId, Comment comment);

    public List<Comment> getCommentOfPost(Integer postId);
}
