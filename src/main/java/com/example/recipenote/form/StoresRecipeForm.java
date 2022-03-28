package com.example.recipenote.form;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.Store;

import java.util.ArrayList;
import java.util.List;

public class StoresRecipeForm {

    private Store store;
    private List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }
}
