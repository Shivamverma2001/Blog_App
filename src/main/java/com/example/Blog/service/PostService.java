package com.example.Blog.service;

import com.example.Blog.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    public Post getPost(Integer postId);

    public List<Post> getPosts();

    public Post savePost(Post post, String tagNames);

    public void deletePostById(Integer id);

    public List<Post> getPostsBySearch(String keyword);

    public Page<Post> findPaginated(Integer pageNumber, Integer pageSize);
}
