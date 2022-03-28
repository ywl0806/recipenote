package com.example.recipenote.repository;

import com.example.recipenote.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByAffiliateId(Long id);

    Page<Store> findAllBy(Pageable pageable);
}