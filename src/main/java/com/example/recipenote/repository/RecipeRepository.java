package com.example.recipenote.repository;

import com.example.recipenote.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByIsPublicTrue();
    List<Recipe> findByAffiliateId(Long affiliateId);
    List<Recipe> findByUserId(Long userId);
    List<Recipe> findByUserIdAndIsPublicTrue(Long userId);
}