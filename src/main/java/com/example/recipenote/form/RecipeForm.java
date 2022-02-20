package com.example.recipenote.form;

import com.example.recipenote.entity.Affiliate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeForm {
    private Long id;
    private String name;
    private String content;
    private Long userId;
    private Long affiliateId;
    private String userName;
    private String storeName;
    private Long storeId;
    private Boolean isPublic;

    public RecipeForm(Long id, String name, String content, Long userId, Long affiliateId, String userName, String storeName, Long storeId, Boolean isPublic) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
        this.affiliateId = affiliateId;
        this.userName = userName;
        this.storeName = storeName;
        this.storeId = storeId;
        this.isPublic = isPublic;
    }

    public RecipeForm() {}
}
