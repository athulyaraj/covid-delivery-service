package com.covid.support.deliveryservice.utils;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public class CommonUtils {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static boolean isEmpty(Collection collection){
        if(collection == null || collection.isEmpty())
            return true;
        return false;
    }

    public static String getSlot(Timestamp startTime, Timestamp endTime){
        Double maxSlots = Math.ceil((endTime.getTime() - startTime.getTime())/ Constants.timeInterval);
        String filled = StringUtils.repeat('0',maxSlots.intValue());
        return filled;
    }

    public static String setSlot(String slot){
        int index = slot.lastIndexOf('1');
        if(index == slot.length()-1)
        {
            log.info("No Slot available");
            throw new CustomException(Constants.SLOTS_NOT_AVAILABLE,Constants.SLOTS_NOT_AVAILABLE_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        char array[] = slot.toCharArray();
        if(index == 0){
            array[index] = '1';
        }else{
            array[index + 1] = '1';
        }
        return new String(array);
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
