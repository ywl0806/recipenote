package com.example.recipenote.controller;

import com.example.recipenote.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        model.addAttribute("message","Hello "+ user.getName());
        return "pages/home";
    }
}
