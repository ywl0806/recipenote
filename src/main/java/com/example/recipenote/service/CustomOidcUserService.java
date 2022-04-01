package com.example.recipenote.service;

import com.example.recipenote.entity.Role;
import com.example.recipenote.entity.SocialUser;
import com.example.recipenote.entity.User;
import com.example.recipenote.repository.AffiliateRepository;
import com.example.recipenote.repository.RoleRepository;
import com.example.recipenote.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Set;


@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);


    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    public CustomOidcUserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        Assert.notNull(userRequest, "userRequest cannot be null");

        OidcUserInfo userInfo = new OidcUserInfo(userRequest.getIdToken().getClaims());
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();

        try{
            OidcUser oidcUser = new DefaultOidcUser(authorities,userRequest.getIdToken());

            String email = oidcUser.getEmail();
            logger.info("user email:  "+ email);
            logger.info("user name:  " + oidcUser.getFullName());

            User user = userRepository.findByUsername(email);

            if (user == null){// userを登録
                user = new User();
                user.setUsername(email);
                user.setPassword("");
                user.addRole(roleRepository.findByName("ROLE_GUEST"));
            }

            user.setAvatarUrl(oidcUser.getPicture());
            user.setName(oidcUser.getFullName());
            user = userRepository.saveAndFlush(user);

            for (Role role : user.getRoles()){
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }

            return new SocialUser(authorities,userRequest.getIdToken(),userInfo,user.getUserId());
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

}
