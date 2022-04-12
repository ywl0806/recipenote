package com.example.recipenote.service;

import com.example.recipenote.controller.AffiliateController;
import com.example.recipenote.entity.Affiliate;
import com.example.recipenote.entity.Role;
import com.example.recipenote.entity.User;
import com.example.recipenote.entity.role.UserRole;
import com.example.recipenote.form.UserForm;
import com.example.recipenote.repository.AffiliateRepository;
import com.example.recipenote.repository.RoleRepository;
import com.example.recipenote.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class AffiliateService {

    private static final Logger logger = LoggerFactory.getLogger(AffiliateService.class);

    @Autowired
    private final AffiliateRepository affiliateRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    public AffiliateService(AffiliateRepository affiliateRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.affiliateRepository = affiliateRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void createAffiliate(Affiliate affiliate, String username, HttpServletRequest request) {
        User user = userRepository.findByUsername(username);
        affiliate = affiliateRepository.save(affiliate);
//        ユーザーにroleを追加
        user.setAffiliate(affiliate);
        user.setAffiliateId(affiliate.getId());
        Role roleManager = roleRepository.findByName("ROLE_MANAGER");
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.addRole(roleManager);
        user.addRole(roleUser);
        user = userRepository.saveAndFlush(user);
//       認証をリロードする
        logger.info("security context updated to {}",user);
        Authentication reAuth = new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(reAuth);
    }

    public List<UserForm> getUserList(Long id) {
        ArrayList<User> userList = userRepository.findByAffiliateId(id);
        if (userList.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<UserForm> userForms = new ArrayList<>();
        for (User user : userList) {
            UserForm form = new UserForm();
            form.setEmail(user.getUsername());
            form.setName(user.getName());
            form.setAvatarUrl(user.getAvatarUrl());
            form.setId(user.getUserId());
            userForms.add(form);
        }
        return  userForms;
    }

    public Map<String, Object> searchMember(String email) {
        User user = userRepository.findByUsername(email);
        if (user == null || user.getAffiliateId() != null) {
            return new HashMap<>();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("email",user.getUsername());
        map.put("name",user.getName());
        map.put("id",user.getUserId());
        map.put("avatarUrl",user.getAvatarUrl());

        return map;
    }

    public void addMember(Long affiliateId, Long userId) {
        Optional<User> user = userRepository.findById(userId);

        user.orElseThrow().setAffiliateId(affiliateId);
        user.orElseThrow().addRole(roleRepository.findByName("ROLE_USER"));

        userRepository.save(user.orElseThrow());
    }

    public String getName(Long affiliateId) {
        Affiliate affiliate = affiliateRepository.findById(affiliateId).orElseThrow();

        return affiliate.getName();
    }
}
