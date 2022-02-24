package com.example.recipenote.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String thumbnailPath;

    @Column
    private Long affiliateId;

    @Column
    private Long storeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean isPublic;

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
    private Set<Ingredient> ingredients = new HashSet<>();

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }
}