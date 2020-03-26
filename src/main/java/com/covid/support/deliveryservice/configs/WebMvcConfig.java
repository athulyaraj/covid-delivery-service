package com.covid.support.deliveryservice.configs;

import com.covid.support.deliveryservice.interceptors.RequestAuthentication;
import com.covid.support.deliveryservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    RequestAuthentication requestAuthentication;
    UserRepository userRepository;

    @Autowired
    public WebMvcConfig(UserRepository userRepository){
        this.requestAuthentication = new RequestAuthentication(userRepository);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestAuthentication);
    }
}
