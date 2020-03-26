package com.covid.support.deliveryservice.models.requests;

import com.covid.support.deliveryservice.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneVerificationVerifyRequest extends BaseModel {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String countryCode;
    @NotNull
    private String token;
}
