package com.example.recipenote.service;

import com.example.recipenote.entity.User;
import com.example.recipenote.entity.role.UserRole;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public void join(UserForm userForm){
        String name = userForm.getName();
        String email = userForm.getEmail();
        String password = userForm.getPassword();
        String passwordConfirmation = userForm.getPasswordConfirmation();


        if (!password.equals(passwordConfirmation)){
            throw new IllegalStateException("passwordが一致しません。");
        }
        if (userRepository.findByUsername(email) != null) {
            throw new IllegalStateException("ユーザーが存在します。");
        }
        User newUser = new User(email,name,passwordEncoder.encode(password), UserRole.ROLE_USER);
        userRepository.save(newUser);
    }

}
