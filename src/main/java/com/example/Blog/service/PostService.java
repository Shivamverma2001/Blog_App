package com.example.Blog.service;

import com.example.Blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface PostService {
    public Post getPost(Integer postId);

    public List<Post> getPosts();

    public Post savePost(Post post, String tagNames);

    public void deletePostById(Integer id);

    public List<Post> getPostsBySearch(String keyword);

    public Page<Post> findPaginated(Model model, Integer pageNumber, String field, String direction);


}
