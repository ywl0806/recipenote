package com.example.recipenote.controller;

import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.User;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class StoresController {

    @Autowired
    private final StoreService storeService;

    public StoresController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/create-store")
    public String create(Model model, Authentication authentication) {
        Store store = new Store();
        model.addAttribute("store", store);
        return "stores/new";
    }

    @PostMapping("/new-store")
    public String newStore(@ModelAttribute("store") Store store, Authentication authentication) {
        UserInf user = (UserInf) authentication.getPrincipal();
        storeService.newStore(store, user.getUsername());
        return "redirect:/";
    }

    @GetMapping("/store-detail")
    public String storeDetail(@RequestParam(name = "id") Long id, Model model) {
        Store store = storeService.getStore(id);

        model.addAttribute("store",store);
        return "stores/detail";
    }
}
