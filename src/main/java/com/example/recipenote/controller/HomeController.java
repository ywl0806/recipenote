package com.example.recipenote.controller;

import com.example.recipenote.entity.UserInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {

    @Autowired
    private final MessageSource messageSource;


    public HomeController(MessageSource messageSource) {
        this.messageSource = messageSource;

    }


    //認証済みではないユーザー、所属してないユーザー：/home
    //認証済みの所属があるユーザー：所属する会社のホーム
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {


        if (authentication != null) {
            UserInf user = (UserInf) authentication.getPrincipal();
            if (user.getAffiliateId() != null) {
                model.addAttribute("affiliateId", user.getAffiliateId());
                return "redirect:/affiliate/" + user.getAffiliateId();
            }
        }
        return "pages/home";
    }

    //接近が拒否される
    @GetMapping(value = "/err/denied-page")
    public String accessDenied(){
        return "pages/denied-page";
    }
}
