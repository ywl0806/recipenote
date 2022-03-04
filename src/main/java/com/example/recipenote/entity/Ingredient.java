package com.example.recipenote.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ingredient")
public class Ingredient extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ingredient_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double price = 0.0;

    @Column(nullable = false)
    private String name;


}