package com.example.recipenote.form;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class UserForm {

    private Long id;
    private String name;
    private String email;
    private String avatarUrl;
    private Long affiliateId;
    private String password;
    private String passwordConfirmation;

    public UserForm(Long id, String name, String email, String avatarUrl, Long affiliateId, String password, String passwordConfirmation) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.affiliateId = affiliateId;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
    public UserForm(){}
}
