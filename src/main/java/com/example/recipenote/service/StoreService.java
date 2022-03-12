package com.example.recipenote.service;

import com.example.recipenote.entity.Store;
import com.example.recipenote.entity.User;
import com.example.recipenote.repository.StoreRepository;
import com.example.recipenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {
    @Autowired
    private final StoreRepository storeRepository;
    @Autowired
    private final UserRepository userRepository;
    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
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


}
