package com.covid.support.deliveryservice.service.impl;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.Verification;
import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.exceptions.CustomException;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.repositories.UserRepository;
import com.covid.support.deliveryservice.service.PhoneVerificationService;
import com.covid.support.deliveryservice.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PhoneVerificationServiceImpl  implements PhoneVerificationService {

    private AuthyApiClient authyApiClient;
    private UserRepository userRepository;

    @Autowired
    public PhoneVerificationServiceImpl(AuthyApiClient authyApiClient,UserRepository userRepository) {
        this.authyApiClient = authyApiClient;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseModel start(String countryCode, String phoneNumber, String via) {
        Params params = new Params();
        params.setAttribute("code_length", "4");
        Verification verification = authyApiClient
                .getPhoneVerification()
                .start(phoneNumber, countryCode, via, params);
        if(!verification.isOk()){
            log.info("Verification failed");
            throw new CustomException(Constants.AUTH_ERROR_CODE,Constants.AUTH_FAILED, HttpStatus.UNAUTHORIZED);
        }
        User user = User.builder().mobileNo(phoneNumber).build();
        user = userRepository.save(user);
        return ResponseModel.builder()
                .data(user)
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE).build();
    }

    @Override
    public ResponseModel verify(String countryCode, String phoneNumber, String token) {
        Verification verification = authyApiClient
                .getPhoneVerification()
                .check(phoneNumber, countryCode, token);
        if(!verification.isOk()){
            log.info("Verification failed");
            throw new CustomException(Constants.AUTH_ERROR_CODE,Constants.AUTH_FAILED, HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findByMobileNo(phoneNumber);
        if(user == null){
            log.info("No user found for mobile no = {}",phoneNumber);
            throw new CustomException(Constants.AUTH_ERROR_CODE,Constants.AUTH_FAILED, HttpStatus.UNAUTHORIZED);
        }
        String genToken = CommonUtils.generateNewToken();
        user.setToken(genToken);
        user = userRepository.save(user);
        return ResponseModel.builder()
                .data(user)
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE).build();
    }
}
