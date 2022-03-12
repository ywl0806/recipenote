package com.example.recipenote.config;

import com.example.recipenote.config.authhandler.AuthFailureHandler;
import com.example.recipenote.config.authhandler.WebAccessDeniedHandler;
import com.example.recipenote.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//@PreAuthorize を使用するため
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final AuthFailureHandler authFailureHandler;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private final AuthorizationDynamicHandler authorizationDynamicHandler;

    @Autowired
    private final WebAccessDeniedHandler webAccessDeniedHandler;

    public WebSecurityConfig(AuthFailureHandler authFailureHandler, UserDetailsServiceImpl userDetailsService, AuthorizationDynamicHandler authorizationDynamicHandler, WebAccessDeniedHandler webAccessDeniedHandler) {
        this.authFailureHandler = authFailureHandler;
        this.userDetailsService = userDetailsService;
        this.authorizationDynamicHandler = authorizationDynamicHandler;
        this.webAccessDeniedHandler = webAccessDeniedHandler;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] URLS = {"/css/**", "/images/**", "/scripts/**", "/upload/**", "/h2-console/**"};


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
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
                    .antMatchers("/login", "/join").anonymous()
                    .antMatchers("/manage-user").hasRole("MANAGER")
                    .antMatchers("/affiliate/{affiliateId}/**")
                        .access("@authorizationDynamicHandler.checkAffiliateId(authentication,#affiliateId)")//ユーザーの所属をチェック
                    .antMatchers("/recipe-detail/**")
                        .access("@authorizationDynamicHandler.checkRecipeDetailPermission(authentication,request)")
                .anyRequest().authenticated()
                    .and().exceptionHandling().accessDeniedHandler(webAccessDeniedHandler)//access拒否のハンドラー　ー＞　メッセージ出力後、homeに戻す
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureHandler(authFailureHandler)
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



}