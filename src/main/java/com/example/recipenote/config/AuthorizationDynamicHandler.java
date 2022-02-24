package com.example.recipenote.config;

import com.example.recipenote.entity.User;
import com.example.recipenote.entity.UserInf;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class AuthorizationDynamicHandler {

    public boolean checkAffiliateId(Authentication authentication,Long affiliateId ){
        Object principal = authentication.getPrincipal();

        if(!(principal instanceof User)){
            return false;
        }
        UserInf user = (UserInf) principal;
        return Objects.equals(user.getAffiliateId(), affiliateId);
    }
}
