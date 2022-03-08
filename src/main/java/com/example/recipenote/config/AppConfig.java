package com.example.recipenote.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {ContextInstanceDataAutoConfiguration.class})
public class AppConfig {
}
