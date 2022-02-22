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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

    //    公開されてない会社のrecipe
    public List<RecipeForm> getNotPublicRecipeList(Long id) {
        List<Recipe> list;
        list = recipeRepository.findByAffiliateId(id);
        return mappingRecipeList(list);
    }

    public Map<String, Object> saveImageLocal(Map<String, Object> paramMap, MultipartRequest image, HttpServletRequest request) throws IOException {

        MultipartFile uploadFile = image.getFile("upload");
        String uploadDir = request.getServletContext().getRealPath("/").replace("\\", "/") + "upload/images/";
        String uploadId = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(Objects.requireNonNull(uploadFile).getOriginalFilename());
        File file = new File(uploadDir + uploadId);

        if (!file.exists()) {
            file.mkdirs();
            uploadFile.transferTo(file);
        }
        paramMap.put("url", "upload/images/" + uploadId);
        return paramMap;
    }
        public List<RecipeForm> mappingRecipeList (List < Recipe > list) {
            List<RecipeForm> recipeForms = new ArrayList<RecipeForm>();
            for (Recipe entity : list) {
                RecipeForm form = RecipeForm.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .userName(entity.getUser().getName())
                        .build();
                recipeForms.add(form);

                if (entity.getStoreId() != null) {
                    form.setStoreName(entity.getStore().getName());
                }

            }
            return recipeForms;
        }

    }
