package com.example.recipenote.controller;

import com.example.recipenote.entity.Ingredient;
import com.example.recipenote.service.IngredientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class IngredientController {

    @Autowired
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @ResponseBody
    @GetMapping("/search-ingredient")
    public List<Ingredient> searchIngredient(String keyword, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {
        PageRequest request = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "name"));

        return ingredientService.searchIngredient(keyword, request);
    }

    @ResponseBody
    @PostMapping("/create-ingredient")
    public String createIngredient(@RequestBody Ingredient ingredient) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(ingredientService.saveIngredient(ingredient));
    }

}
