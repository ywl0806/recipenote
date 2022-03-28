package com.example.recipenote.controller;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.service.RecipeService;
import com.example.recipenote.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private final MessageSource messageSource;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final RecipeService recipeService;
    public HomeController(MessageSource messageSource, StoreService storeService, RecipeService recipeService) {
        this.messageSource = messageSource;
        this.storeService = storeService;
        this.recipeService = recipeService;
    }


    //認証済みではないユーザー、所属してないユーザー：/home
    //認証済みの所属があるユーザー：所属する会社のホーム
    @GetMapping("/")
    public String home(Model model, Authentication authentication, @RequestParam(defaultValue = "0", name = "page", required = false)  int page) {

        if (authentication !=null) {
            UserInf user = (UserInf) authentication.getPrincipal();
            if (user.getAffiliateId() != null) {
                model.addAttribute("affiliateId", user.getAffiliateId());
                return "redirect:/affiliate/" + user.getAffiliateId();
            }
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Map<Store, List<Recipe>>map = new HashMap<>();
        Page<Store> stores = storeService.getStores(sort, page);

        for (Store store : stores.getContent()) {
            List<Recipe> recipes = recipeService.getStoresRecipes(sort, 0, store.getId());
            map.put(store, recipes);
        }

        logger.info(map.toString());

        model.addAttribute("totalPage", stores.getTotalPages());
        model.addAttribute("map", map);
        return "pages/home";
    }

    //接近が拒否される
    @GetMapping(value = "/err/denied-page")
    public String accessDenied(){
        return "pages/denied-page";
    }
}
