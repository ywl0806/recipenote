package com.example.recipenote.config.authhandler;

import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.*;

@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private final MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(WebAccessDeniedHandler.class);

    public WebAccessDeniedHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //cookieからlocale情報を読み込む
        Cookie[] cookies = request.getCookies();
        String lang = "en";
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "USER_LOCALE")) {
                lang = cookie.getValue();
                break;
            }
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        //messageをセットする。
        List<String> messages= new ArrayList<>();
        messages.add(messageSource.getMessage("WebAccessDeniedHandler.message", null, Locale.forLanguageTag(lang)));
        request.setAttribute("messages",messages);
        request.setAttribute("class", "alert-danger");
        request.setAttribute("hasMessage", true);

        logger.info("Access Denied");
        //errページへforwordで移動
        request.getRequestDispatcher("/err/denied-page").forward(request,response);
    }
}
