package com.example.Blog.service;

import com.example.Blog.model.Post;
import com.example.Blog.model.Tag;
import com.example.Blog.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    public final TagService tagService;

    public PostServiceImpl(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    @Override
    public Post getPost(Integer postId) {
        return this.postRepository.findById(postId).orElse(null);
    }

    @Override
    public List<Post> getPosts() {
        return this.postRepository.findAll();
    }

    @Override
    public Post savePost(Post post, String tagNames) {
        post.getTags().clear();
        HashSet<String> uniqueTagNames = new HashSet<>();

        for(String tagName: tagNames.split(",")) {
            uniqueTagNames.add(tagName.trim().toLowerCase());
        }

        for (String tagName: uniqueTagNames) {
            Tag tag = this.tagService.findByName(tagName);

            if (tag != null ) {
                post.addTag(tag);
            } else {
                Tag newTag = this.tagService.createTag(new Tag(tagName));
                post.addTag(newTag);
            }
        }

        return this.postRepository.save(post);
    }

    @Override
    public void deletePostById(Integer id) {
        this.postRepository.deleteById(id);
    }

    @Override
    public List<Post> getPostsBySearch(String keyword) {
        return this.postRepository.searchByKeyword(keyword);
    }

    @Override
    public Page<Post> findPaginated(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return this.postRepository.findAll(pageable);
    }


}
