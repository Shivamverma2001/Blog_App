package com.example.Blog.repository;

import com.example.Blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT * FROM comments  WHERE post_id = :postId ORDER BY id", nativeQuery = true)
    List<Comment> getCommentOfPost(Integer postId);
}
