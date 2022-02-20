package com.example.recipenote.controller;

import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.service.RecipeService;
import com.example.recipenote.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RecipesController {

    @Autowired
    private final RecipeService recipeService;
    @Autowired
    private final StoreService storeService;
    public RecipesController(RecipeService recipeService, StoreService storeService) {
        this.recipeService = recipeService;
        this.storeService = storeService;
    }

    @GetMapping("/recipe-detail")
    public String detail(Model model, Authentication authentication, @RequestParam Long id){
        RecipeForm recipe = recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipes/detail";
    }
    @GetMapping("/recipes")
    public String index(Model model, Authentication authentication){

        List<RecipeForm> list = recipeService.getAllPublicRecipeList();
        model.addAttribute("list", list);
        return "recipes/index";
    }

    @GetMapping("/new-recipe")
    public String writeRecipe(Model model, Authentication authentication) {
        RecipeForm form = new RecipeForm();
        UserInf user = (UserInf) authentication.getPrincipal();
        if (user.getAffiliateId() != null){
            List<Store> storeList = storeService.getStoreList(user.getAffiliateId());
            model.addAttribute("storeList",storeList);
        }
        model.addAttribute("form", form);
        return "recipes/new";
    }

    @PostMapping("/create-recipe")
    public String createRecipe(Authentication authentication ,@ModelAttribute("form")RecipeForm form){
        UserInf user = (UserInf) authentication.getPrincipal();
        form.setUserId(user.getUserId());
        if (user.getAffiliateId()!=null){
            form.setAffiliateId(user.getAffiliateId());
        }
        if (form.getIsPublic()==null){
            form.setIsPublic(true);
        }
        recipeService.newRecipe(form);
        return "redirect:/recipes";
    }
}
