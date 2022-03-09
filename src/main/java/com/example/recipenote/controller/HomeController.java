package com.example.recipenote.controller;

import com.example.recipenote.entity.UserInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;



@Controller
public class HomeController {

    @Autowired
    private final MessageSource messageSource;

    public HomeController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {

        if (authentication != null) {
            UserInf user = (UserInf) authentication.getPrincipal();
            System.out.println(authentication.getAuthorities());
            if (user.getAffiliateId() != null) {
                model.addAttribute("affiliateId", user.getAffiliateId());
                return "redirect:/affiliate/" + user.getAffiliateId();
            }
        }
        return "pages/home";
    }
}
