package com.example.recipenote.repository;

import com.example.recipenote.entity.AmountOfIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmountOfIngredientRepository extends JpaRepository<AmountOfIngredient, Long> {
    List<AmountOfIngredient> findByRecipeId(Long recipeId);
    List<AmountOfIngredient> findByIngredientId(Long ingredientId);
}