package com.covid.support.deliveryservice.controllers;

import com.covid.support.deliveryservice.entities.Store;
import com.covid.support.deliveryservice.models.ResponseModel;
import com.covid.support.deliveryservice.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api")
public class StoreController {

    StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    @PostMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE , consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel createStore(@Valid @RequestBody Store store){
        return storeService.createStore(store);
    }

    @PostMapping(value = "/store/{store_id}", produces = MediaType.APPLICATION_JSON_VALUE , consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel updateStoreDetails(@Valid @PathVariable("store_id")Integer storeId, @RequestBody Store store){
        return storeService.updateStoreDetails(store,storeId);
    }

    @GetMapping(value = "/store/{store_id}", produces = MediaType.APPLICATION_JSON_VALUE , consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseModel getOrders(@PathVariable("store_id")Integer storeId){
        return storeService.getOrders(storeId);
    }

}
