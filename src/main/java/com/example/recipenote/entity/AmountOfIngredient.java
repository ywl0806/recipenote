package com.example.recipenote.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AmountOfIngredient {

    @Id
    @SequenceGenerator(name = "store_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double amount = 0.0;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
}
