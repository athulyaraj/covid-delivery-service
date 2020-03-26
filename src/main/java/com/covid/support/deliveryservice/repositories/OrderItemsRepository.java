package com.covid.support.deliveryservice.repositories;

import com.covid.support.deliveryservice.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Integer> {
}
