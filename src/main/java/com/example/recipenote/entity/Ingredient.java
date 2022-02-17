package com.example.recipenote.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ingredient")
public class Ingredient extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ingredient_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int price;

    @Column(nullable = false)
    private String name;
}