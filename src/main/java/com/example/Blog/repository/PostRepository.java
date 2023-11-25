package com.example.Blog.repository;

import com.example.Blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    public Page<Post> findAllByOrderByPublishedAtAsc(Pageable pageable);

    public Page<Post> findAllByOrderByPublishedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE (:authors IS NULL OR p.author IN :authors) " +
            "AND (:tags IS NULL OR EXISTS (SELECT tg.name FROM p.tags tg WHERE tg.name IN :tags)) " +
            "ORDER BY p.publishedAt ASC")
    public List<Post> filterAndSortByAuthorTagPublishedAtAsc(List<String> authors, List<String> tags);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "WHERE (:authors IS NULL OR p.author IN :authors) " +
            "AND (:tags IS NULL OR EXISTS (SELECT tg.name FROM p.tags tg WHERE tg.name IN :tags)) " +
            "ORDER BY p.publishedAt DESC")
    public List<Post> filterAndSortByAuthorTagPublishedAtDesc(List<String> authors, List<String> tags);

    @Query("select p from Post p " +
            "LEFT JOIN p.tags t " +
            "where (:authors is null or p.author in :authors) " +
            "AND (:tags IS NULL OR EXISTS (SELECT tg.name FROM p.tags tg WHERE tg.name IN :tags)) ")
    public List<Post> filterByAuthorTag(List<String> authors, List<String> tags);

}
