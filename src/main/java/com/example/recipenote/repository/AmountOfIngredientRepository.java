package com.example.recipenote.repository;

import com.example.recipenote.entity.AmountOfIngredient;
import com.example.recipenote.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AmountOfIngredientRepository extends JpaRepository<AmountOfIngredient, Long> {
    List<AmountOfIngredient> findByRecipeId(Long recipeId);
    List<AmountOfIngredient> findByIngredientId(Long ingredientId);
    List<AmountOfIngredient> findByIngredientNameContaining(String keyword);
}