package com.example.recipenote.form;

import com.example.recipenote.validation.PasswordEquals;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Builder
@Data
@PasswordEquals
public class UserForm {

    private Long id;
    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    private String avatarUrl;

    private Long affiliateId;

    @NotEmpty
    private String password;

    @NotEmpty
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
