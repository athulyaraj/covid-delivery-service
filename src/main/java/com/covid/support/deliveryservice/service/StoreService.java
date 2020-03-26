package com.covid.support.deliveryservice.service;

import com.covid.support.deliveryservice.entities.Store;
import com.covid.support.deliveryservice.models.ResponseModel;

public interface StoreService {

    public ResponseModel createStore(Store store);
    public ResponseModel updateStoreDetails(Store store,Integer storeId);
    public ResponseModel getOrders(Integer storeId);
}
