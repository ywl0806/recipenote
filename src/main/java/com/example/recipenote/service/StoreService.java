package com.example.recipenote.service;

import com.example.recipenote.entity.Recipe;
import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.User;
import com.example.recipenote.form.StoresRecipeForm;
import com.example.recipenote.repository.RecipeRepository;
import com.example.recipenote.repository.StoreRepository;
import com.example.recipenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreService {


    @Autowired
    private final StoreRepository storeRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RecipeRepository recipeRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public void newStore(Store store, String userName){
        User user = userRepository.findByUsername(userName);
        store.setAffiliateId(user.getAffiliateId());
        storeRepository.save(store);
    }

    public List<Store> getStoreList(Long affiliateId){
        if ( storeRepository.findByAffiliateId(affiliateId) == null){
            throw new IllegalStateException("Not found store");
        }
        return storeRepository.findByAffiliateId(affiliateId);
    }

    public Store getStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow();

        return store;
    }


    public Page<Store> getStores(Sort sort,int page){
        Pageable pageable = PageRequest.of(page, 4, sort);

        return storeRepository.findAllBy(pageable);
    }
}
