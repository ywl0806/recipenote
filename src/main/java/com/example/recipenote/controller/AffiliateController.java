package com.example.recipenote.controller;

import com.example.recipenote.entity.Affiliate;
import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.service.AffiliateService;
import com.example.recipenote.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AffiliateController {

    @Autowired
    private final AffiliateService affiliateService;
    @Autowired
    private final RecipeService recipeService;

    public AffiliateController(AffiliateService affiliateService, RecipeService recipeService) {
        this.affiliateService = affiliateService;
        this.recipeService = recipeService;
    }

    @GetMapping("/create-affiliate")
    public String create(Model model) {
        Affiliate affiliate = new Affiliate();
        model.addAttribute("affiliate", affiliate);
        return "affiliates/new";
    }

    @PostMapping("/new-affiliate")
    public String newAffiliate(Model model, @ModelAttribute("affiliate") Affiliate affiliate, Authentication authentication) {
        UserInf user = (UserInf) authentication.getPrincipal();
        affiliateService.createAffiliate(affiliate, user.getUsername());

        return "redirect:/create-store";
    }

    @GetMapping("/affiliate/{id}")
    public String index(Model model, @PathVariable Long id) {
        List<RecipeForm> list = recipeService.getNotPublicRecipeList(id);
        model.addAttribute("list", list);
        return "affiliates/index";
    }
}
