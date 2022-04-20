package com.example.recipenote.controller;

import com.example.recipenote.entity.AmountOfIngredient;
import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.AmountOfIngredientForm;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.service.AwsS3Service;
import com.example.recipenote.service.IngredientService;
import com.example.recipenote.service.RecipeService;
import com.example.recipenote.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@Slf4j
public class RecipesController {

    @Autowired
    private final RecipeService recipeService;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final AwsS3Service awsS3Service;
    @Autowired
    private final IngredientService ingredientService;

    public RecipesController(RecipeService recipeService, StoreService storeService, AwsS3Service awsS3Service, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.storeService = storeService;
        this.awsS3Service = awsS3Service;
        this.ingredientService = ingredientService;
    }

    @Value("${image.local}")
    private Boolean imageLocal;

    @GetMapping("/recipe-detail")
    public String detail(Model model, Authentication authentication, @RequestParam Long id) {
        RecipeForm recipe = recipeService.getRecipe(id);
        List<AmountOfIngredient> list = ingredientService.getIngredientList(id);

        model.addAttribute("recipe", recipe);
        model.addAttribute("list", list);
        return "recipes/detail";
    }

    @GetMapping("/recipes")
    public String index(Model model, @RequestParam(defaultValue = "0", name = "page", required = false) int page) {
//        Set<Recipe> recipes = recipeService.searchRecipe("파");
//        for (Recipe recipe : recipes) {
//            if (recipe != null){
//                System.out.println("레시피  " + recipe.getName());
//            }
//        }
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Recipe> pageList = recipeService.getAllPublicRecipeList(pageable);
        int totalPage = pageList.getTotalPages();

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("list", pageList.getContent());
        return "recipes/index";
    }


    @GetMapping("/new-recipe")
    public String writeRecipe(Model model, Authentication authentication) {
        RecipeForm form = new RecipeForm();
        UserInf user = (UserInf) authentication.getPrincipal();
        if (user.getAffiliateId() != null) {
            List<Store> storeList = storeService.getStoreList(user.getAffiliateId());
            model.addAttribute("storeList", storeList);
        }

        for (int i = 0; i < 30; i++) {
            form.addIngredient(new AmountOfIngredientForm());
        }
        model.addAttribute("form", form);
        return "recipes/new";
    }

    // アップロードの流れ：　thumbnailとレシピを作成（レシピのイメージは非同期処理でアップロード）　→　アップロード　→　thumbnailをscailing　→　サーバーに保存　→　パスのみdbの保存
    @PostMapping("/create-recipe")
    public String createRecipe(Authentication authentication, @ModelAttribute("form") RecipeForm form) throws Exception {
        UserInf user = (UserInf) authentication.getPrincipal();
        form.setUserId(user.getUserId());
        if (user.getAffiliateId() != null) {
            form.setAffiliateId(user.getAffiliateId());
        }
        if (form.getIsPublic() == null) {
            form.setIsPublic(true);
        }
        recipeService.newRecipe(form);
        return "redirect:/recipes";
    }

    @ResponseBody
    @PostMapping("/upload-image")
    public Map<String, Object> uploadImage(
            @RequestParam Map<String, Object> paramMap, MultipartRequest image) throws Exception {
        MultipartFile file = image.getFile("upload");
        String path;
        if (imageLocal) {
            path = recipeService.saveImageLocal(file);
        } else {
//            path = awsS3Service.uploadImage(file,"/images");
            path = awsS3Service.uploadFiles(file, "images");
        }
        paramMap.put("url", path);
        return paramMap;
    }

    @ResponseBody
    @PostMapping("/upload-thumbnail")
    public String uploadThumbnail(MultipartFile avatar) {
        if (!avatar.isEmpty()) {
            if (imageLocal) {
                return recipeService.saveThumbnailLocal(avatar);
            } else {
                try {
                    return awsS3Service.uploadFiles(avatar, "thumbnails");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        return "";
    }

    @GetMapping("/search")
    public String searchRecipe(Model model, @RequestParam(name = "keyword", required = false) String keyword, @RequestParam(defaultValue = "0", name = "page", required = false) int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Recipe> pageList = recipeService.searchRecipe(keyword, pageable);

        model.addAttribute("totalPage", pageList.getTotalPages());
        model.addAttribute("list", pageList.getContent());
        return "recipes/index";
    }

    @GetMapping("/remove-recipe")
    public String removeRecipe(Model model, Authentication authentication, @RequestParam Long id) {
        UserInf user = (UserInf) authentication.getPrincipal();
        ArrayList<String> messages = new ArrayList<>();
        log.info("recipe id: {}", id);
        try {
            recipeService.removeRecipe(id, user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            messages.add(e.getMessage());
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("messages", messages);
            return "pages/denied-page";
        }

        messages.add("remove success!");
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-success");
        model.addAttribute("messages", messages);
        return "pages/denied-page";
    }

}
