package com.example.recipenote.form;

import lombok.Data;

@Data
public class UserForm {

    private String name;
    private String email;
    private String password;
    private String passwordConfirmation;
}
