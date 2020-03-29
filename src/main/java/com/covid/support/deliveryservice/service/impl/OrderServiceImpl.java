package com.covid.support.deliveryservice.service.impl;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.Order;
import com.covid.support.deliveryservice.entities.OrderItems;
import com.covid.support.deliveryservice.entities.Store;
import com.covid.support.deliveryservice.entities.User;
import com.covid.support.deliveryservice.enums.OrderStatus;
import com.covid.support.deliveryservice.exceptions.CustomException;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.repositories.OrderItemsRepository;
import com.covid.support.deliveryservice.repositories.OrderRepository;
import com.covid.support.deliveryservice.repositories.StoreRepository;
import com.covid.support.deliveryservice.repositories.UserRepository;
import com.covid.support.deliveryservice.service.OrderService;
import com.covid.support.deliveryservice.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderItemsRepository orderItemsRepository;
    StoreRepository storeRepository;
    CommonUtils commonUtils;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, OrderItemsRepository orderItemsRepository, CommonUtils commonUtils){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.commonUtils = commonUtils;
    }

    @Override
    public ResponseModel createOrder(Order order) {
        Integer userId = Integer.parseInt(MDC.get(Constants.MDC_USER_ID));
        List<Order> existingOrders = orderRepository.findByOrderStatusInAndUserId(Arrays.asList(new OrderStatus[]{OrderStatus.PLACED, OrderStatus.PROCESSING}), userId);
        if (!CommonUtils.isEmpty(existingOrders)) {
            log.info("Order exists for userId = {}", userId);
            throw new CustomException(Constants.OPERATION_NOT_PERMITTED, Constants.OPERATION_NOT_PERMITTED_MESSAGE, HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(userId).orElse(null);
        order.setUser(user);
        order.setOrderLocation(new Point(user.getLon(),user.getLat()));
        order = orderRepository.save(order);
        return ResponseModel.builder()
                .data(order)
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE)
                .build();
    }

    @Override
    public ResponseModel verifyOrderItems(Order order) {
        List<OrderItems> orderItemsList = order.getOrderItems();
        if(CommonUtils.isEmpty(orderItemsList)){
            log.info("No order items to verify");
            throw new CustomException(Constants.DATA_NOT_FOUND,Constants.DATA_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        for(OrderItems orderItems : orderItemsList){
            orderItemsRepository.save(orderItems);
        }
        return ResponseModel.builder()
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE)
                .build();
    }

    @Override
    public ResponseModel updateStatus(Order order) {
        Integer userId = Integer.parseInt(MDC.get(Constants.MDC_USER_ID));
        if(order.getId() == null || order.getOrderStatus() == null){
            log.info("Order id/status not present");
            throw new CustomException(Constants.REQUIRED_PARAMS_NOT_PRESENT,Constants.REQUIRED_PARAMS_NOT_PRESENT_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        log.info("Updating order_id = {} status = {}",order.getId(),order.getOrderStatus().name());
        int updatedStatus = orderRepository.updateOrderStatus(order.getOrderStatus(),order.getId());
        if(updatedStatus < 0){
            log.info("Updating order status failed for id = {}",order.getId());
            throw new CustomException(Constants.INTERNAL_ERROR,Constants.INTERNAL_ERROR_MESSAGE,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(OrderStatus.PROCESSING.equals(order.getOrderStatus())){
            getSlotOfStore(userId,order.getId());
        }
        return ResponseModel.builder()
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE)
                .build();
    }

    @Override
    public ResponseModel getOrderDetails(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return ResponseModel.builder()
                .message(Constants.SUCCESS_MESSAGE)
                .status(Constants.SUCCESS_CODE)
                .data(order)
                .build();
    }

    @Async
    public void getSlotOfStore(Integer userId,Integer orderId){
        try {
            int status = 0;
            do {
                Store store = storeRepository.findByUserId(userId);
                Integer slotNo = commonUtils.setSlot(store.getId());
                Timestamp startTime = new Timestamp(store.getOperationalStartTime().getTime() + slotNo*Constants.timeInterval);
                Timestamp endTime = new Timestamp(startTime.getTime() + Constants.timeInterval);
                orderRepository.updateStartTimeAndEndTime(startTime,endTime,orderId);
            } while (status < 0);
        }catch (Exception ex){
            log.error("Exception happened while updating slots for orderId = {} exception = {}",orderId,ex);
            orderRepository.updateOrderStatus(OrderStatus.FAILED,orderId);
        }
    }
}
