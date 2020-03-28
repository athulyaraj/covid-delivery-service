package com.covid.support.deliveryservice.repositories;

import com.covid.support.deliveryservice.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store,Integer> {
    Store findByUserId(Integer userId);

    @Query("update Store s set s.slots = :slot where s.id = :storeId")
    int updateSlotsInStore(@Param("slot") Integer slot, @Param("storeId")Integer storeId);
}
