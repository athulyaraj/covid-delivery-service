package com.covid.support.deliveryservice.service;

import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.models.ResponseModel;

public interface PhoneVerificationService {

    public ResponseModel start(String countryCode, String phoneNumber, String via);
    public ResponseModel verify(String countryCode, String phoneNumber, String token);
}
