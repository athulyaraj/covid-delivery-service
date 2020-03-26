package com.covid.support.deliveryservice.repositories;

import com.covid.support.deliveryservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByToken(String token);
    User findByMobileNo(String mobileNo);
}
