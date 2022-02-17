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
    private Long storeId;

    public RecipeForm(Long id, String name, String content,Long userId , Long affiliateId, Long storeId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
        this.affiliateId = affiliateId;
        this.storeId = storeId;
    }


    public RecipeForm() {}
}
