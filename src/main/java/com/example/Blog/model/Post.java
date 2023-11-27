package com.example.Blog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User author;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();



    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
}
