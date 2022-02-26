package com.example.recipenote.controller;

import com.example.recipenote.entity.User;
import com.example.recipenote.entity.UserInf;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

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
