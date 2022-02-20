package com.example.recipenote.service;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.User;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.repository.RecipeRepository;
import com.example.recipenote.repository.StoreRepository;
import com.example.recipenote.repository.UserRepository;
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
        entity.setIsPublic(form.getIsPublic());
        entity.setAffiliateId(form.getAffiliateId());
        entity.setStoreId(form.getStoreId());
        recipeRepository.save(entity);
    }

    public RecipeForm getRecipe(Long id) {
        Recipe entity = recipeRepository.getById(id);
        RecipeForm form = RecipeForm.builder()
                .id(id)
                .name(entity.getName())
                .content(entity.getContent())
                .isPublic(entity.getIsPublic())
                .build();
        return form;
    }
//      公開されたrecipe
    public List<RecipeForm> getAllPublicRecipeList() {
        List<Recipe> list;
        list = recipeRepository.findByIsPublicTrue();

        return mappingRecipeList(list);
    }

//    公開されてない会社のrecipe
    public List<RecipeForm> getNotPublicRecipeList(Long id) {
        List<Recipe> list;
        list = recipeRepository.findByAffiliateId(id);
        return mappingRecipeList(list);
    }

    public List<RecipeForm >mappingRecipeList(List<Recipe> list){
        List<RecipeForm> recipeForms = new ArrayList<RecipeForm>();
        for (Recipe entity : list) {
            RecipeForm form = RecipeForm.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .userName(entity.getUser().getName())
                    .build();
            recipeForms.add(form);

            if (entity.getStoreId()!=null){
                form.setStoreName(entity.getStore().getName());
            }

        }
        return recipeForms;
    }
}
