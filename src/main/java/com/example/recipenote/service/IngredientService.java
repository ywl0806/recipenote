package com.example.recipenote.service;

import com.example.recipenote.entity.Ingredient;
import com.example.recipenote.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class IngredientService {

    @Autowired
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
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


}
