package com.example.recipenote.service;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.User;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.repository.RecipeRepository;
import com.example.recipenote.repository.UserRepository;
import com.example.recipenote.service.tools.SaveImage;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class RecipeService {

    @Autowired
    private final RecipeRepository recipeRepository;
    @Autowired
    private final UserRepository userRepository;

    private static String localResourcePath = "C:/Users/ywl08/resource/";

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public void newRecipe(RecipeForm form) {
        Recipe entity = new Recipe();
        entity.setName(form.getName());
        entity.setContent(form.getContent());
        entity.setUserId(form.getUserId());
        entity.setIsPublic(form.getIsPublic());
        entity.setAffiliateId(form.getAffiliateId());
        entity.setStoreId(form.getStoreId());
        entity.setThumbnailPath(form.getThumbnailPath());
        recipeRepository.save(entity);
    }

    public RecipeForm getRecipe(Long id) {
        Recipe entity = recipeRepository.getById(id);
        return RecipeForm.builder()
                .id(id)
                .name(entity.getName())
                .content(entity.getContent())
                .isPublic(entity.getIsPublic())
                .build();
    }

    //      公開されたrecipe
    public List<RecipeForm> getAllPublicRecipeList() {
        List<Recipe> list;
        list = recipeRepository.findByIsPublicTrue();

        return mappingRecipeList(list);
    }

    public List<RecipeForm> getPersonalRecipes(Long targetId, UserInf userInf) throws Exception{
        Optional<User> targetUser = userRepository.findById(targetId);
        List<Recipe> list;
        if (targetUser.isPresent()){
            UserInf tUser = targetUser.get();

            if (Objects.equals(tUser.getAffiliateId(), userInf.getAffiliateId())){
                list = recipeRepository.findByUserId(tUser.getUserId());
                return mappingRecipeList(list);
            }

            list = recipeRepository.findByUserIdAndIsPublicTrue(targetId);
            return mappingRecipeList(list);
        }
        return null;
    }

    //    会社のrecipe
    public List<RecipeForm> getNotPublicRecipeList(Long id) {
        List<Recipe> list;
        list = recipeRepository.findByAffiliateId(id);
        return mappingRecipeList(list);
    }

    public String saveThumbnailLocal(MultipartFile image){

        return SaveImage.saveThumbnail(image,"/thumbnails",400,400);
    }

    public Map<String, Object> saveImageLocal(Map<String, Object> paramMap, MultipartFile image) throws IOException {
        String path = SaveImage.saveImage(image,"/images");
        paramMap.put("url",path);

        return paramMap;
    }
        public List<RecipeForm> mappingRecipeList (List < Recipe > list) {
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

    }
