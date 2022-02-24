package com.example.recipenote.controller;

import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.service.RecipeService;
import com.example.recipenote.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;


@Controller
public class RecipesController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final RecipeService recipeService;
    @Autowired
    private final StoreService storeService;
    public RecipesController(RecipeService recipeService, StoreService storeService) {
        this.recipeService = recipeService;
        this.storeService = storeService;
    }
    @Value("${image.local}")
    private Boolean imageLocal;

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
// アップロードの流れ：　thumbnailとレシピを作成（レシピのイメージは非同期処理でアップロード）　→　アップロード　→　thumbnailをscailing　→　サーバーに保存　→　パスのみdbの保存
    @PostMapping("/create-recipe")
    public String createRecipe(Authentication authentication , @ModelAttribute("form")RecipeForm form, MultipartFile image,HttpServletRequest request) throws Exception{
        UserInf user = (UserInf) authentication.getPrincipal();
        form.setUserId(user.getUserId());
        if (user.getAffiliateId()!=null){
            form.setAffiliateId(user.getAffiliateId());
        }
        if (form.getIsPublic()==null){
            form.setIsPublic(true);
        }
        if (!image.isEmpty()){
            String thumbnailPath = recipeService.saveThumbnailLocal(image,request);
            form.setThumbnailPath(thumbnailPath);
        }

        recipeService.newRecipe(form);
        return "redirect:/recipes";
    }

    @ResponseBody
    @PostMapping("/upload/image")
    public Map<String,Object> uploadImage(
            @RequestParam Map<String, Object> paramMap, MultipartRequest image, HttpServletRequest request) throws Exception{
       if (imageLocal){
           paramMap = recipeService.saveImageLocal(paramMap,image,request);
       }
        return paramMap;
    }
}
