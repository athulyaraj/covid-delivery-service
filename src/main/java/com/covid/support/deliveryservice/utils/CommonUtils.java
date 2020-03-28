package com.covid.support.deliveryservice.utils;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.Store;
import com.covid.support.deliveryservice.exceptions.CustomException;
import com.covid.support.deliveryservice.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
public class CommonUtils {

    StoreRepository storeRepository;

    @Autowired
    public CommonUtils(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static boolean isEmpty(Collection collection){
        if(collection == null || collection.isEmpty())
            return true;
        return false;
    }

    public static Integer getSlot(Timestamp startTime, Timestamp endTime){
        Double maxSlots = Math.ceil((endTime.getTime() - startTime.getTime())/ Constants.timeInterval);
        return maxSlots.intValue();
    }

    public Integer setSlot(Integer storeId){
        synchronized (storeId){
            Store store = storeRepository.findById(storeId).orElse(null);
            int noOfSlots = store.getSlots();
            if(noOfSlots == store.getMaxSlots()){
                log.info("No slots available");
                throw new CustomException(Constants.SLOTS_NOT_AVAILABLE,Constants.SLOTS_NOT_AVAILABLE_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
            }
            store.setSlots(noOfSlots+1);
            storeRepository.save(store);
            return noOfSlots+1;
        }
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
