package com.example.Blog.service;

import com.example.Blog.model.Post;
import com.example.Blog.model.Tag;
import com.example.Blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final static Integer pageSize = 6;


    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagService tagService, UserService userService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    @Override
    public Post getPost(Integer postId) {
        return this.postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post savePost(Post post, String tagNames) {
        post.getTags().clear();
        Set<String> uniqueTagNames = new HashSet<>();

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

    public Page<Post> getPaginatedFilteredPosts(Model model, Integer pageNumber, String field, String direction,
                                                String authors, String tags) {

        List<String> authorList = null;
        List<String> tagList = null;

        if (authors != null && !authors.isEmpty()) {
            authorList = new ArrayList<>();
            for(String author: authors.split(",")) {
                authorList.add(author.trim());
            }
        }

        if (tags != null && !tags.isEmpty()) {
            tagList = new ArrayList<>();
            for(String tagName: tags.split(",")) {
                tagList.add(tagName.trim().toLowerCase());
            }
        }

        List<Post> filteredPosts;
        if (field == null) {
            filteredPosts = postRepository.filterByAuthorTag(authorList, tagList);
        } else if (direction.equals("asc")) {
            filteredPosts = postRepository.filterAndSortByAuthorTagPublishedAtAsc(authorList, tagList);
        } else {
            filteredPosts = postRepository.filterAndSortByAuthorTagPublishedAtDesc(authorList, tagList);
        }

        int startItem = (pageNumber-1) * PostServiceImpl.pageSize;
        List<Post> pagedPosts;

        if (filteredPosts.size() < startItem) {
            pagedPosts = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + PostServiceImpl.pageSize, filteredPosts.size());
            pagedPosts = filteredPosts.subList(startItem, toIndex);
        }

        model.addAttribute("totalPages", (int) Math.ceil((double)filteredPosts.size()/PostServiceImpl.pageSize));
        model.addAttribute("totalPosts", filteredPosts.size());

        return new PageImpl<>(pagedPosts, PageRequest.of(pageNumber, PostServiceImpl.pageSize), filteredPosts.size());
    }

    @Override
    public Page<Post> getPosts(Model model, Integer pageNumber, String field, String direction,
                               String authors, String tags, String search) {
        Pageable pageable = PageRequest.of(pageNumber-1, PostServiceImpl.pageSize);
        Page<Post> page;
        boolean filterFlag = false;

        if (search != null && !search.isEmpty() && !search.equals("null")) {
            page = postRepository.searchByKeyword(search, pageable);

        } else if ((authors != null && !authors.isEmpty() && !authors.equals("null")) ||
                (tags != null && !tags.isEmpty() && !tags.equals("null"))) {
            filterFlag = true;
            page = this.getPaginatedFilteredPosts(model, pageNumber, field, direction, authors, tags);

        } else if (field != null && field.equals("publishedAt") && direction.equals("asc")){
            page = postRepository.findAllByOrderByPublishedAtAsc(pageable);

        } else if (field != null && field.equals("publishedAt") && direction.equals("desc")) {
            page = postRepository.findAllByOrderByPublishedAtDesc(pageable);

        } else {
            page = postRepository.findAll(pageable);
        }

        model.addAttribute("posts", page.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("authors", authors);
        model.addAttribute("tags", tags);
        model.addAttribute("search", search);

        if (!filterFlag) {
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalPosts", page.getTotalElements());
        }

        return page;
    }

}
