package com.example.Blog.service;

import com.example.Blog.model.Comment;

public interface CommentService {
    public void create(Comment comment);

    public Comment findCommentById(Integer id);

    public void deleteById(Integer id);

    public Comment update(Comment comment);
}
