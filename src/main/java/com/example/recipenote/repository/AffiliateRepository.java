package com.example.recipenote.repository;

import com.example.recipenote.entity.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliateRepository extends JpaRepository<Affiliate, Long> {
    Affiliate findByName(String name);
}