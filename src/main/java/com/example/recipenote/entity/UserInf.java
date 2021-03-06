package com.example.recipenote.entity;

public interface UserInf {
    Long getUserId();

    String getUsername();

    Long getAffiliateId();

    String getName();

    String getAvatarUrl();

    Affiliate getAffiliate();
}
