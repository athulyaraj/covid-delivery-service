package com.covid.support.deliveryservice.service.impl;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.Order;
import com.covid.support.deliveryservice.entities.Store;
import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.enums.OrderStatus;
import com.covid.support.deliveryservice.exceptions.CustomException;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.repositories.OrderRepository;
import com.covid.support.deliveryservice.repositories.StoreRepository;
import com.covid.support.deliveryservice.repositories.UserRepository;
import com.covid.support.deliveryservice.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StoreServiceImpl implements com.covid.support.deliveryservice.service.StoreService {

    StoreRepository storeRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository,OrderRepository orderRepository,UserRepository userRepository){
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseModel createStore(Store store) {
        Integer userId = Integer.parseInt(MDC.get(Constants.MDC_USER_ID));
        User user = userRepository.findById(userId).orElse(null);
        store.setUser(user);
        store.setSlots(CommonUtils.getSlot(store.getOperationalStartTime(),store.getOperationalEndTime()));
        store = storeRepository.save(store);
        return ResponseModel.builder()
                .data(store)
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE)
                .build();
    }

    @Override
    public ResponseModel updateStoreDetails(Store store, Integer storeId) {
        Integer userId = Integer.parseInt(MDC.get(Constants.MDC_USER_ID));
        Store s = storeRepository.findById(storeId).orElse(null);
        if(s == null){
            log.info("Store not available for id = {}",storeId);
            throw new CustomException(Constants.DATA_NOT_FOUND,String.format(Constants.DATA_NOT_FOUND_MESSAGE,storeId), HttpStatus.BAD_REQUEST);
        }
        if(s.getUser().getId() != userId){
            log.info("UserId = {} not authorized to update",userId);
            throw new CustomException(Constants.OPERATION_NOT_PERMITTED,Constants.OPERATION_NOT_PERMITTED_MESSAGE,HttpStatus.FORBIDDEN);
        }
        store.setId(storeId);
        store = storeRepository.save(store);
        return ResponseModel.builder()
                .status(Constants.SUCCESS_CODE)
                .message(Constants.SUCCESS_MESSAGE)
                .data(store)
                .build();
    }

    @Override
    public ResponseModel getOrders(Integer storeId) {
        List<Order> orderList = orderRepository.findByOrderStatus(OrderStatus.PLACED);
        return ResponseModel.builder()
                .status(Constants.SUCCESS_CODE)
                .message(Constants.SUCCESS_MESSAGE)
                .data(orderList)
                .build();
    }
}
