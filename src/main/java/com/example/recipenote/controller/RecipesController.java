package com.example.recipenote.controller;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.service.RecipeService;
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

    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe-detail")
    public String detail(Model model, Authentication authentication, @RequestParam Long id){
        RecipeForm recipe = recipeService.getRecipe(id);
        model.addAttribute("recipe",recipe);
        return "recipes/detail";
    }
    @GetMapping("/recipes")
    public String index(Model model, Authentication authentication){

        List<RecipeForm> list = recipeService.getAllRecipeList();
        model.addAttribute("list", list);
        return "recipes/index";
    }

    @GetMapping("/write-recipe")
    public String writeRecipe(Model model, Authentication authentication) {
        RecipeForm form = new RecipeForm();
        model.addAttribute("form", form);
        return "recipes/new";
    }

    @PostMapping("/create-recipe")
    public String createRecipe(Model model, Authentication authentication ,@ModelAttribute("form")RecipeForm form){
        UserInf user = (UserInf) authentication.getPrincipal();
        form.setUserId(user.getUserId());
        recipeService.newRecipe(form);
        return "redirect:/recipes";
    }
}
