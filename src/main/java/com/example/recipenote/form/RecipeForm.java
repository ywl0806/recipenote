package com.example.recipenote.form;

import com.example.recipenote.entity.Affiliate;
import com.example.recipenote.entity.Ingredient;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RecipeForm {
    private Long id;
    private String name;
    private String content;
    private Long userId;
    private Long affiliateId;
    private String thumbnailPath;
    private String userName;
    private String storeName;
    private Long storeId;
    private Boolean isPublic;
    private List<AmountOfIngredientForm> ingredients = new ArrayList<>();

    public RecipeForm(Long id, String name, String content, Long userId, Long affiliateId,String thumbnailPath, String userName, String storeName, Long storeId, Boolean isPublic, List<AmountOfIngredientForm> ingredients) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
        this.affiliateId = affiliateId;
        this.thumbnailPath = thumbnailPath;
        this.userName = userName;
        this.storeName = storeName;
        this.storeId = storeId;
        this.isPublic = isPublic;
        this.ingredients = ingredients;
    }

    public void addIngredient(AmountOfIngredientForm ingredient){
        this.ingredients.add(ingredient);
    }

    public RecipeForm() {}
}
