package com.covid.support.deliveryservice.controllers;

import com.covid.support.deliveryservice.constants.Constants;
import com.covid.support.deliveryservice.entities.Order;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api")
public class OrderController {

    OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping(path = "/order", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel placeOrder(@Valid @RequestBody Order order){
        return orderService.createOrder(order);
    }

    @PutMapping(path = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel verifyOrderItems(@Valid @RequestBody Order order){
        return orderService.verifyOrderItems(order);
    }

    @PutMapping(path = "/order/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel updateStatus(@Valid @RequestBody Order order){
        return orderService.updateStatus(order);
    }

    @GetMapping(path = "/order/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel getOrderDetails(@PathVariable("id") Integer orderId){
        return orderService.getOrderDetails(orderId);
    }
}
