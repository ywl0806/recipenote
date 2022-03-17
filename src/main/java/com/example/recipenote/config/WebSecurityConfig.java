package com.example.recipenote.config;

import com.example.recipenote.config.authhandler.AuthFailureHandler;
import com.example.recipenote.config.authhandler.AuthSuccessHandler;
import com.example.recipenote.config.authhandler.WebAccessDeniedHandler;

import com.example.recipenote.service.CustomOauth2UserService;
import com.example.recipenote.service.CustomOidcUserService;
import com.example.recipenote.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)//@PreAuthorize を使用するため
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);


    @Autowired
    private final AuthFailureHandler authFailureHandler;

    @Autowired
    private final AuthSuccessHandler authSuccessHandler;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private final AuthorizationDynamicHandler authorizationDynamicHandler;

    @Autowired
    private final WebAccessDeniedHandler webAccessDeniedHandler;

    @Autowired
    private final CustomOauth2UserService customOauth2UserService;

    @Autowired
    private final CustomOidcUserService customOidcUserService;


    public WebSecurityConfig(AuthFailureHandler authFailureHandler, AuthSuccessHandler authSuccessHandler, UserDetailsServiceImpl userDetailsService, AuthorizationDynamicHandler authorizationDynamicHandler, WebAccessDeniedHandler webAccessDeniedHandler, CustomOauth2UserService customOauth2UserService, CustomOidcUserService customOidcUserService) {
        this.authFailureHandler = authFailureHandler;
        this.authSuccessHandler = authSuccessHandler;
        this.userDetailsService = userDetailsService;
        this.authorizationDynamicHandler = authorizationDynamicHandler;
        this.webAccessDeniedHandler = webAccessDeniedHandler;
        this.customOauth2UserService = customOauth2UserService;
        this.customOidcUserService = customOidcUserService;
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
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login").and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureHandler(authFailureHandler)
                    .permitAll()
                    .and()
                .oauth2Login()
                    .loginPage("/login")
                    .successHandler(authSuccessHandler)//userServiceからのuser情報じゃなくてdbからuser情報をcontextに入れるため
                    .permitAll()
                    .userInfoEndpoint()
                        .userService(customOauth2UserService)
                        .oidcUserService(customOidcUserService);

    }
    @Bean
    public JwtDecoderFactory<ClientRegistration> idTokenecoderFactory() {
        OidcIdTokenDecoderFactory idTokenDecoderFactory = new OidcIdTokenDecoderFactory();
        idTokenDecoderFactory.setJwsAlgorithmResolver(clientRegistration -> MacAlgorithm.HS256);
        return idTokenDecoderFactory;
    }


    //Authentication -> login
    //Authroization -> 権限の設定



}