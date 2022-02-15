package com.example.recipenote.controller;

import com.example.recipenote.form.UserForm;
import com.example.recipenote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {


    @Autowired
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
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
    public String postJoin(Model model, @ModelAttribute("form") UserForm userForm){
        userService.join(userForm);
        return "users/login";
    }
}
