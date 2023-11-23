package com.example.Blog.repository;

import com.example.Blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE p.title LIKE %:keyword% OR " +
            "p.content LIKE %:keyword% OR " +
            "p.author LIKE %:keyword% OR " +
            "t.name LIKE %:keyword%")
    List<Post> searchByKeyword(@Param("keyword") String keyword);
}
