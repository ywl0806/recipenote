package com.example.recipenote.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAutoConfiguration(exclude = {ContextInstanceDataAutoConfiguration.class})
public class WebConfig implements WebMvcConfigurer {

    // ファイルをlocalに保存するときpathの設定
//    @Value("${resource.path}")
//    private String resourcePath;
//
//    @Value("${upload.path}")
//    private String uploadPath;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(uploadPath)
//                .addResourceLocations(resourcePath);
//    }
}
