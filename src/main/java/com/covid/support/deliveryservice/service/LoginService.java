package com.covid.support.deliveryservice.service;

import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.models.ResponseModel;

public interface LoginService {

    ResponseModel createLogin(User user);
    ResponseModel validateLogin(String token);
}
