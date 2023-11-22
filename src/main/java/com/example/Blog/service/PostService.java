package com.example.Blog.service;

import com.example.Blog.model.Post;

import java.util.List;

public interface PostService {
    public Post getPost(Integer postId);

    public List<Post> getPosts();

    public void createPost(Post post);

    public void updatePost(Post post);
}
