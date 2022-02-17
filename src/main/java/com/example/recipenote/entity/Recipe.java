package com.example.recipenote.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recipe")
@NoArgsConstructor
public class Recipe extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "recipe_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column
    private Long affiliateId;

    @Column
    private Long storeId;

    @Column(nullable = false)
    private Long userId;


    @OneToOne
    @JoinColumn(name = "affiliateId", insertable = false, updatable = false)
    private Affiliate affiliate;

    @OneToOne
    @JoinColumn(name = "storeId", insertable = false, updatable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "recipeId", insertable = false, updatable = false)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "RECIPE_INGREDIENT",
            joinColumns = @JoinColumn(name = "RECIPE_ID"),
            inverseJoinColumns = @JoinColumn(name = "INGREDIENT_ID"))
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();
}