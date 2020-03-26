package com.covid.support.deliveryservice.service.impl;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.exceptions.CustomException;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.repositories.UserRepository;
import com.covid.support.deliveryservice.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public ResponseModel createLogin(User user) {
        log.info("Creating user details");
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        existingUser = updateUser(existingUser,user);
        existingUser = userRepository.save(existingUser);
        return ResponseModel.builder()
                .status(Constants.SUCCESS_CODE)
                .message(Constants.SUCCESS_MESSAGE)
                .data(existingUser).build();
    }

    public User updateUser(User existingUser,User updateUser){
        if(updateUser.getLat() != null){
            existingUser.setLat(updateUser.getLat());
        }
        if(updateUser.getLon() != null){
            existingUser.setLon(updateUser.getLon());
        }
        if(updateUser.getAddress() != null){
            existingUser.setAddress(updateUser.getAddress());
        }
        if(updateUser.getName() != null){
            existingUser.setName(updateUser.getName());
        }
        if(updateUser.getUserType() != null){
            existingUser.setUserType(updateUser.getUserType());
        }
        return existingUser;
    }

    @Override
    public ResponseModel validateLogin(String token) {
        User user = userRepository.findByToken(token);
        if(user == null){
            log.info("User is not found for token");
            throw new CustomException(Constants.AUTH_ERROR_CODE,Constants.AUTH_FAILED, HttpStatus.UNAUTHORIZED);
        }
        return ResponseModel.builder()
                .data(user)
                .status(Constants.SUCCESS_CODE)
                .message(Constants.SUCCESS_MESSAGE)
                .build();
    }
}
