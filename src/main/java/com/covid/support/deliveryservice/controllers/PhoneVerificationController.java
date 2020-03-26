package com.covid.support.deliveryservice.controllers;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.models.requests.PhoneVerificationStartRequest;
import com.covid.support.deliveryservice.models.requests.PhoneVerificationVerifyRequest;
import com.covid.support.deliveryservice.repositories.UserRepository;
import com.covid.support.deliveryservice.service.PhoneVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/phone-verification")
public class PhoneVerificationController {

    private PhoneVerificationService phoneVerificationService;

    @Autowired
    public PhoneVerificationController(PhoneVerificationService phoneVerificationService) {
        this.phoneVerificationService = phoneVerificationService;
    }

    @PostMapping(path = "/start",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel start(@Valid @RequestBody PhoneVerificationStartRequest requestBody) {
            ResponseModel responseModel = phoneVerificationService.start(requestBody.getCountryCode(),
                    requestBody.getPhoneNumber(),
                    requestBody.getVia());
            return responseModel;
    }

    @PostMapping(path = "/verify",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel verify(@Valid @RequestBody PhoneVerificationVerifyRequest requestBody,
                                HttpSession session) {
        ResponseModel responseModel =phoneVerificationService.verify(requestBody.getCountryCode(),
                requestBody.getPhoneNumber(),
                requestBody.getToken());
        session.setAttribute("ph_verified", true);
        return responseModel;
    }
}
