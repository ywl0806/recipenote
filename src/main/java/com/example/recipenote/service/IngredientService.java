package com.example.recipenote.service;

import com.example.recipenote.entity.AmountOfIngredient;
import com.example.recipenote.entity.Ingredient;
import com.example.recipenote.repository.AmountOfIngredientRepository;
import com.example.recipenote.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class IngredientService {

    @Autowired
    private final IngredientRepository ingredientRepository;

    @Autowired
    private final AmountOfIngredientRepository amountOfIngredientRepository;

    public IngredientService(IngredientRepository ingredientRepository, AmountOfIngredientRepository amountOfIngredientRepository) {
        this.ingredientRepository = ingredientRepository;
        this.amountOfIngredientRepository = amountOfIngredientRepository;
    }

    @Transactional
    public Ingredient saveIngredient(Ingredient ingredient) {

        ingredientRepository.save(ingredient);


        return ingredient;
    }

    @Transactional
    public List<Ingredient> searchIngredient(String keyword, PageRequest request){

        return ingredientRepository.findByNameContaining(keyword,request);

    }

    public List<AmountOfIngredient> getIngredientList(Long recipeId) {
        List<AmountOfIngredient> list = amountOfIngredientRepository.findByRecipeId(recipeId);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return list;
    }


}
