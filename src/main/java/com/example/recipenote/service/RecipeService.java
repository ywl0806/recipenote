package com.example.recipenote.service;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void newRecipe(RecipeForm form) {
        Recipe entity = new Recipe();
        entity.setName(form.getName());
        entity.setContent(form.getContent());
        entity.setUserId(form.getUserId());
        recipeRepository.save(entity);
    }

    public RecipeForm getRecipe(Long id) {
        Recipe entity = recipeRepository.getById(id);
        RecipeForm form = RecipeForm.builder()
                .id(id)
                .name(entity.getName())
                .content(entity.getContent()).build();
        return form;
    }

    public List<RecipeForm> getAllRecipeList() {
        List<Recipe> list;
        list = recipeRepository.findAll();
        List<RecipeForm> recipeForms = new ArrayList<RecipeForm>();
        for (Recipe entity : list) {
            RecipeForm form = RecipeForm.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .content(entity.getContent())
                    .build();
            recipeForms.add(form);
        }
        return recipeForms;
    }

}
