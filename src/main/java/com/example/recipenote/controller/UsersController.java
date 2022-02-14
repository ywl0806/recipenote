package com.example.recipenote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    @GetMapping("/login")
    public String index(Model model) {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(Model model) {
        //로그인 성공시 "/"
        //로그인 실패시 메세지 출력 후 "users/login"
        return "/";
    }
}
