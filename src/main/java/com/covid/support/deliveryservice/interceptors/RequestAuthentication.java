package com.covid.support.deliveryservice.interceptors;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.exceptions.CustomException;
import com.covid.support.deliveryservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestAuthentication extends HandlerInterceptorAdapter {

    UserRepository userRepository;

    @Autowired
    public RequestAuthentication(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        User user = userRepository.findByToken(token);
        if(user == null){
            log.info("user not found");
            throw new CustomException(Constants.AUTH_ERROR_CODE, Constants.AUTH_FAILED, HttpStatus.UNAUTHORIZED);
        }
        return true;
    }
}
