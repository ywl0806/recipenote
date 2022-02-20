package com.example.recipenote.service;

import com.example.recipenote.entity.Affiliate;
import com.example.recipenote.entity.Role;
import com.example.recipenote.entity.User;
import com.example.recipenote.entity.role.UserRole;
import com.example.recipenote.repository.AffiliateRepository;
import com.example.recipenote.repository.RoleRepository;
import com.example.recipenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AffiliateService {
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

    public void createAffiliate(Affiliate affiliate, String username) {
        User user = userRepository.findByUsername(username);
        affiliateRepository.save(affiliate);
//        ユーザーにroleを追加
        user.setAffiliateId(affiliateRepository.findByName(affiliate.getName()).getId());
        Role roleManager = roleRepository.findByName("ROLE_MANAGER");
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.addRole(roleManager);
        user.addRole(roleUser);
        userRepository.save(user);
//       認証をリロードする
        Authentication reAuth = new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(reAuth);
    }

}
