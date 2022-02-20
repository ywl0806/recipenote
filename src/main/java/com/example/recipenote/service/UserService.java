package com.example.recipenote.service;

import com.example.recipenote.entity.Role;
import com.example.recipenote.entity.User;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.repository.RoleRepository;
import com.example.recipenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        User newUser = new User(email,name,passwordEncoder.encode(password));
        Role role = roleRepository.findByName("ROLE_GUEST");
        newUser.addRole(role);
        userRepository.save(newUser);
    }

}
