package com.example.recipenote.config.authhandler;

import com.example.recipenote.entity.User;
import com.example.recipenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private final UserRepository userRepository;

    public AuthSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = userRepository.findByUsername(authentication.getName());

        if (user.getAffiliateId() != null) {
            setDefaultTargetUrl("/affiliate/"+user.getAffiliateId());
        }else{
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request,response,authentication);
    }
}
