package com.example.recipenote.controller;

import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.RecipeForm;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.service.RecipeService;
import com.example.recipenote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UsersController {


    @Autowired
    private final UserService userService;
    @Autowired
    private final RecipeService recipeService;

    public UsersController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @GetMapping("/login")
    public String index(Model model) {
        return "users/login";
    }


    @GetMapping("/join")
    public String getJoin(Model model) {
        model.addAttribute("form", new UserForm());
        return "users/join";
    }

    @PostMapping("/join")
    public String postJoin(@ModelAttribute("form") UserForm userForm){
        userService.join(userForm);
        return "users/login";
    }

    @GetMapping("/me")
    public String getMe(Model model, Authentication authentication) throws Exception {

        UserInf userInf = (UserInf) authentication.getPrincipal();
        UserForm userForm = userService.getMe(userInf.getUserId());
        List<RecipeForm> list = recipeService.getPersonalRecipes(userInf.getUserId(),userInf);

        model.addAttribute("form", userForm);
        model.addAttribute("list", list);

        return "users/me";
    }
    @PostMapping("/update-me")
    public String updateMe(RedirectAttributes redirectAttributes, @ModelAttribute("form")UserForm form, MultipartFile avatar){
        Long userId = userService.updateMe(form,avatar);
        System.out.println("파일:  " + avatar.isEmpty());
        System.out.println("유저:  " + form.toString());
        //redirectAttributes.addFlashAttribute("id",userId);
        return "redirect:/";
    }
    @GetMapping("/user-detail")
    public String getUserDetail(Model model, Authentication authentication, @RequestParam Long id) throws Exception {
        //타인의 아바타 이미지, 이름, 공개된 레시피 조회기능
        UserInf userInf = (UserInf) authentication.getPrincipal();
        UserForm userForm = userService.getUserDetail(id);
        List<RecipeForm> list = recipeService.getPersonalRecipes(id,userInf);

        model.addAttribute("userForm",userForm);
        model.addAttribute("list",list);

        return "users/detail";
    }
}
