package com.covid.support.deliveryservice.service;

import com.covid.support.deliveryservice.entities.Order;
import com.covid.support.deliveryservice.models.ResponseModel;

public interface OrderService {

    public ResponseModel createOrder(Order order);
    public ResponseModel verifyOrderItems(Order order);
    public ResponseModel updateStatus(Order order);
    public ResponseModel getOrderDetails(Integer orderId);
}
