package com.example.recipenote.repository;


import com.example.recipenote.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findByIsPublicTrue(Pageable pageable);
    Page<Recipe> findByAffiliateId(Long affiliateId,Pageable pageable);
    List<Recipe> findByUserId(Long userId);
    List<Recipe> findByUserIdAndIsPublicTrue(Long userId);
    @Query(value = "select distinct r " +
            "from Recipe r " +
            "left join fetch AmountOfIngredient aoi on r.id = aoi.recipe.id " +
            "left join fetch Ingredient i on i.id = aoi.ingredient.id " +
            "where r.name like %:keyword% or i.name like %:keyword% and r.isPublic = true ")
    Page<Recipe> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}