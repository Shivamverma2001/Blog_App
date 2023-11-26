package com.example.Blog.service;

import com.example.Blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public Post getPost(Integer postId);

    public Page<Post> getPosts(Model model, Integer pageNumber, String field, String direction,
                               String authors, String tags, String search);

    public Post savePost(Post post, String tagNames);

    public void deletePostById(Integer id);

    public Page<Post> getPaginatedFilteredPosts(Model model, Integer pageNumber, String field, String direction,
                              String authors, String tags);

}
