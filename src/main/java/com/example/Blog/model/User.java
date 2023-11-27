package com.example.Blog.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "is_active")
    private boolean is_active;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;


    public User(String username, String email, String name, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = encodedPassword;
    }
}
