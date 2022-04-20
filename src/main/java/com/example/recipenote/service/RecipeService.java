package com.example.recipenote.service;


import com.example.recipenote.entity.*;
import com.example.recipenote.form.AmountOfIngredientForm;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.repository.AmountOfIngredientRepository;
import com.example.recipenote.repository.IngredientRepository;
import com.example.recipenote.repository.RecipeRepository;
import com.example.recipenote.repository.UserRepository;
import com.example.recipenote.service.utils.SaveImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class RecipeService {


    @Autowired
    private final RecipeRepository recipeRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AmountOfIngredientRepository amountOfIngredientRepository;
    @Autowired
    private final IngredientRepository ingredientRepository;
    private static String localResourcePath = "C:/Users/ywl08/resource/";

    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);


    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, AmountOfIngredientRepository amountOfIngredientRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.amountOfIngredientRepository = amountOfIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public void newRecipe(RecipeForm form) {
        Recipe recipe = new Recipe();
        recipe.setName(form.getName());
        recipe.setContent(form.getContent());
        recipe.setUserId(form.getUserId());
        recipe.setIsPublic(form.getIsPublic());
        recipe.setAffiliateId(form.getAffiliateId());
        recipe.setStoreId(form.getStoreId());
        if (!Objects.equals(form.getThumbnailPath(), "")) {
            recipe.setThumbnailPath(form.getThumbnailPath());
        }

        for (AmountOfIngredientForm amountForm : form.getIngredients()) {
            if (amountForm.getIngredientId() != null) {
                logger.info(amountForm.toString());
                Ingredient ingredient = ingredientRepository.getById(amountForm.getIngredientId());
                AmountOfIngredient amount = new AmountOfIngredient();
                amount.setAmount(amountForm.getAmount());
                amount.setIngredient(ingredient);
                recipe.addIngredient(amountOfIngredientRepository.saveAndFlush(amount));
            }
        }

        recipeRepository.saveAndFlush(recipe);
    }

    public RecipeForm getRecipe(Long id) {
        Recipe entity = recipeRepository.getById(id);
        return RecipeForm.builder()
                .id(id)
                .userId(entity.getUserId())
                .name(entity.getName())
                .content(entity.getContent())
                .isPublic(entity.getIsPublic())
                .thumbnailPath(entity.getThumbnailPath())
                .build();
    }

    //      公開されたrecipe
    public Page<Recipe> getAllPublicRecipeList(Pageable pageable) {
        Page<Recipe> page = recipeRepository.findByIsPublicTrue(pageable);
        return page;
    }

    public List<RecipeForm> getPersonalRecipes(Long targetId, UserInf userInf) throws Exception {
        Optional<User> targetUser = userRepository.findById(targetId);
        List<Recipe> list;
        if (targetUser.isPresent()) {
            UserInf tUser = targetUser.get();

            if (Objects.equals(tUser.getAffiliateId(), userInf.getAffiliateId())) {
                list = recipeRepository.findByUserId(tUser.getUserId());
                return mappingRecipeList(list);
            }

            list = recipeRepository.findByUserIdAndIsPublicTrue(targetId);
            return mappingRecipeList(list);
        }
        return null;
    }

    //    会社のrecipe
    public Page<Recipe> getNotPublicRecipeList(Long id, Pageable pageable) {
        Page<Recipe>list = recipeRepository.findByAffiliateId(id,pageable);

        return list;
    }

    public String saveThumbnailLocal(MultipartFile image) {

        return SaveImage.saveThumbnail(image, "/thumbnails", 300, 300);
    }

    public String saveImageLocal(MultipartFile image) throws IOException {
        String path = SaveImage.saveImage(image, "/images");
        return path;
    }

    public List<RecipeForm> mappingRecipeList(List<Recipe> list) {
        List<RecipeForm> recipeForms = new ArrayList<RecipeForm>();
        for (Recipe entity : list) {
            RecipeForm form = RecipeForm.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .userName(entity.getUser().getName())
                    .thumbnailPath(entity.getThumbnailPath())
                    .build();
            recipeForms.add(form);

            if (entity.getStoreId() != null) {
                form.setStoreName(entity.getStore().getName());
            }

        }
        return recipeForms;
    }

    //材料name、レシピnameでレシピを検索
    public Page<Recipe> searchRecipe(String keyword,Pageable pageable) {

        return recipeRepository.findByKeyword(keyword, pageable);
    }

    public List<Recipe> getStoresRecipes(Sort sort, int page , Long storeId) {

        Pageable pageable = PageRequest.of(page, 4, sort);
        List<Recipe> recipes = recipeRepository.findByStoreIdAndIsPublicTrue(pageable, storeId).getContent();
        if (recipes.isEmpty()) {
            return new ArrayList<>();
        }
        return recipes;
    }

    public void removeRecipe(Long recipeId, Long userId) throws Exception {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        if (Objects.equals(recipe.getUserId(), userId)) {
            recipeRepository.delete(recipe);
        } else {
            throw new Exception("Not the owner of the recipe");
        }

    }
}
