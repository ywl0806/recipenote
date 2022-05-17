package com.example.recipenote.controller;

import com.example.recipenote.entity.Affiliate;
import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.service.AffiliateService;
import com.example.recipenote.service.RecipeService;
import com.example.recipenote.service.StoreService;
import com.example.recipenote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class AffiliateController {

    private static final Logger logger = LoggerFactory.getLogger(AffiliateController.class);

    @Autowired
    private final AffiliateService affiliateService;
    @Autowired
    private final RecipeService recipeService;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final MessageSource messageSource;

    public AffiliateController(AffiliateService affiliateService, RecipeService recipeService, StoreService storeService, UserService userService, MessageSource messageSource) {
        this.affiliateService = affiliateService;
        this.recipeService = recipeService;
        this.storeService = storeService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/create-affiliate")
    public String create(Model model) {
        Affiliate affiliate = new Affiliate();
        model.addAttribute("affiliate", affiliate);
        return "affiliates/new";
    }

    @PostMapping("/new-affiliate")
    public String newAffiliate(Model model, @ModelAttribute("affiliate") Affiliate affiliate, Authentication authentication, HttpServletRequest request) {
        UserInf user = (UserInf) authentication.getPrincipal();
        logger.info("userInf : " + user.toString());
        affiliateService.createAffiliate(affiliate, user.getUsername(), request);

        return "redirect:/create-store";
    }


    @GetMapping("/affiliate/{id}")
    public String index(Model model, @PathVariable Long id, @RequestParam(defaultValue = "0", name = "page", required = false) int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Recipe> list = recipeService.getNotPublicRecipeList(id, pageable);

        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("list", list.getContent());
        return "affiliates/index";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/manage-user")
    public String manageUserRoute(Authentication authentication) {
        UserInf user = (UserInf) authentication.getPrincipal();
        if (user.getAffiliateId() != null) {
            return "redirect:/affiliate/" + user.getAffiliateId() + "/manage-user";
        }
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/affiliate/{id}/manage-user")
    public String manageUser(Model model, @PathVariable Long id) {
        List<UserForm> forms = affiliateService.getUserList(id);
        model.addAttribute("list", forms);
        return "affiliates/manage-user";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/manage-store")
    public String manageStoreRoute(Authentication authentication) {
        UserInf user = (UserInf) authentication.getPrincipal();
        if (user.getAffiliateId() != null) {
            return "redirect:/affiliate/" + user.getAffiliateId() + "/manage-store";
        }
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/affiliate/{id}/manage-store")
    public String manageStore(Model model, @PathVariable Long id) {
        List<Store> stores = storeService.getStoreList(id);

        model.addAttribute("list", stores);
        return "affiliates/manage-store";
    }

    @ResponseBody
    @GetMapping("/search-member")
    public Map<String, Object> searchMember(String email) {
        System.out.println(email);
        return affiliateService.searchMember(email);
    }

    @GetMapping("/new-member")
    public String newMember(@RequestParam("memberId") Long memberId, Authentication authentication) {
        UserInf userInf = (UserInf) authentication.getPrincipal();
        Long affiliateId = userInf.getAffiliateId();
        affiliateService.addMember(affiliateId, memberId);
        return "redirect:/manage-user";
    }

    @GetMapping("/register-member")
    public String registerMemberRoute(Authentication authentication) {
        UserInf user = (UserInf) authentication.getPrincipal();
        if (user.getAffiliateId() != null) {
            return "redirect:/affiliate/" + user.getAffiliateId() + "/manage-user/register-member";
        }
        return "redirect:/";
    }

    @GetMapping("affiliate/{id}/manage-user/register-member")
    public String getRegisterMember(@PathVariable Long id, Model model) {
        UserForm userForm = new UserForm();
        userForm.setAffiliateId(id);
        model.addAttribute("user", userForm);
        return "affiliates/register-member";
    }

    @PostMapping("/register-member")
    public String postRegisterMember(Model model ,RedirectAttributes attributes, @Validated @ModelAttribute("user") UserForm userForm, BindingResult result, Locale locale) {

        ArrayList<String> errorMessages = new ArrayList<>();

        if (!userService.checkUserDuplicates(userForm.getEmail())) {
            FieldError fieldError = new FieldError(result.getObjectName(), "email", messageSource.getMessage("users.create.error.duplicates", null, locale));
            result.addError(fieldError);
        }
        result.getAllErrors().forEach(e -> {
            errorMessages.add(e.getDefaultMessage());
        });
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("messages", errorMessages);
            model.addAttribute("redirectUrl","/register-member");

            return "pages/error-page";
        }
        Long userId = userService.join(userForm);
        affiliateService.addMember(userForm.getAffiliateId(),userId);
        return "redirect:/manage-user";
    }
}
