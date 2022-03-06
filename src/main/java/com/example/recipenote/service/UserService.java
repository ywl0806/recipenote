package com.example.recipenote.service;

import com.example.recipenote.entity.Role;
import com.example.recipenote.entity.User;
import com.example.recipenote.entity.UserInf;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.repository.RoleRepository;
import com.example.recipenote.repository.UserRepository;
import com.example.recipenote.service.tools.SaveImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String localResourcePath = "C:/Users/ywl08/resource/";

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void join(UserForm userForm){
        String name = userForm.getName();
        String email = userForm.getEmail();
        String password = userForm.getPassword();

        User newUser = new User(email,name,passwordEncoder.encode(password));
        Role role = roleRepository.findByName("ROLE_GUEST");
        newUser.addRole(role);
        userRepository.save(newUser);
    }

    public UserForm getMe(Long id){
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            User usr = user.get();
            return UserForm.builder()
                    .name(usr.getName())
                    .affiliateId(usr.getAffiliateId())
                    .email(usr.getUsername())
                    .avatarUrl(usr.getAvatarUrl())
                    .build();
        }
        return new UserForm();
    }
    public Long updateMe(UserForm form, MultipartFile avatar){
        User user = userRepository.findByUsername(form.getEmail());
        if (user==null){
            return null;
        }
        user.setName(form.getName());
        if (!avatar.isEmpty()) {
            user.setAvatarUrl(saveUserAvatar(avatar));
        }
        userRepository.saveAndFlush(user);

//       認証をリロードする
        Authentication reAuth = new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(reAuth);
        return user.getUserId();
    }


    public UserForm getUserDetail(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User usr = user.get();
            return UserForm.builder()
                    .avatarUrl(usr.getAvatarUrl())
                    .name(usr.getName())
                    .build();
        }
        return new UserForm();
    }

    public String saveUserAvatar(MultipartFile avatar){
        return SaveImage.saveThumbnail(avatar,"/avatars",500,500);
    }

    public Boolean checkUserDuplicates(String email){
        return userRepository.findByUsername(email) == null;
    }
}
