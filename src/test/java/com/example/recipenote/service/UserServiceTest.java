package com.example.recipenote.service;

import com.example.recipenote.entity.User;
import com.example.recipenote.entity.role.UserRole;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

   @Test
    void join() {
       UserForm userForm = new UserForm();
       userForm.setEmail("e@e");
       userForm.setName("a");
       userForm.setPassword("123");
       userForm.setPasswordConfirmation("123");
       userService.join(userForm);

       User user = userRepository.findByUsername("e@e");
       Assertions.assertThat(user.isEnabled());
   }
}
