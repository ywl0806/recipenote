package com.example.recipenote.config;

import com.example.recipenote.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FormAuthenticationProvider authenticationProvider;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private final AuthorizationDynamicHandler authorizationDynamicHandler;

    @Autowired
    private DataSource dataSource;


    private static final String[] URLS = { "/css/**", "/images/**", "/scripts/**","/upload/**","/h2-console/**"};

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthorizationDynamicHandler authorizationDynamicHandler) {
        this.userDetailsService = userDetailsService;
        this.authorizationDynamicHandler = authorizationDynamicHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login","/join").anonymous()
                    .antMatchers("/affiliate/{affiliateId}/**").access("@authorizationDynamicHandler.checkAffiliateId(authentication,#affiliateId)")
                    .antMatchers("/recipe-detail/**").access("@authorizationDynamicHandler.checkRecipeDetailPermission(authentication,request)")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");
                    //.permitAll();
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider);
//    }
    //Authentication -> login
    //Authroization -> 権限の設定
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }
}