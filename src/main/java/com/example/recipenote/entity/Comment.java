package com.example.recipenote.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comment")
public class Comment extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "comment_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long recipeId;

    @Column(nullable = false)
    private String description;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "recipeId", insertable = false, updatable = false)
    private Recipe recipe;

}