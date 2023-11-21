package com.example.Blog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Title could not be empty")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Excerpt could not be empty")
    @Column(name = "excerpt", length = 1000)
    private String excerpt;

    @NotEmpty(message = "Content could not be empty")
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "published_at")
    @CreationTimestamp
    private Date publishedAt;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
}
