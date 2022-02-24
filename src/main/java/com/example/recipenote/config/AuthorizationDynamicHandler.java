package com.example.recipenote.config;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.User;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthorizationDynamicHandler {

    @Autowired
    private final RecipeRepository recipeRepository;

    public AuthorizationDynamicHandler(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public boolean checkAffiliateId(Authentication authentication,Long affiliateId ) throws Exception{
        Object principal = authentication.getPrincipal();

        if(!(principal instanceof User)){
            return false;
        }
        UserInf user = (UserInf) principal;
        return Objects.equals(user.getAffiliateId(), affiliateId);
    }
    public boolean checkRecipeDetailPermission(Authentication authentication, HttpServletRequest request) throws Exception{
        Optional<Recipe> recipe = recipeRepository.findById(Long.valueOf(request.getParameter("id")));
        Object principal = authentication.getPrincipal();
        UserInf user = (UserInf) principal;

        if (recipe.isPresent()){
            if (recipe.get().getIsPublic()){
                return true;
            }
            return Objects.equals(recipe.get().getAffiliateId(), user.getAffiliateId());
        }
        return false;
    }


}
