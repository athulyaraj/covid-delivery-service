package com.covid.support.deliveryservice.repositories;

import com.covid.support.deliveryservice.entities.Order;
import com.covid.support.deliveryservice.enums.OrderStatus;
import com.vividsolutions.jts.geom.Geometry;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    @Modifying
    @Transactional
    @Query("update Order o set o.orderStatus = :status where o.id = :id")
    int updateOrderStatus(@Param("status") OrderStatus orderStatus,@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Order o set o.startTime = :startTime, o.endTime = :endTime where o.id = :id")
    int updateStartTimeAndEndTime(@Param("startTime")Timestamp startTime, @Param("endTime")Timestamp endTime, @Param("id") Integer orderId);

    List<Order> findByOrderStatusInAndUserId(List<OrderStatus> orderStatus,Integer userId);

    @Query("select o from Order o where within(o.orderLocation, :circle) = true")
    List<Order> findByLocation(@Param("circle") Geometry geometry);

}
