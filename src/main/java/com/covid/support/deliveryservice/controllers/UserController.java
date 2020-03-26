package com.covid.support.deliveryservice.controllers;

import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {

    LoginService loginService;

    @Autowired
    public UserController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping(path = "/user/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel createLogin(@Valid @PathVariable("user_id") Integer userId, @RequestBody User user){
        return loginService.createLogin(user);
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel validateAndGet(@RequestHeader("token")String token){
        return loginService.validateLogin(token);
    }

}
