package com.covid.support.deliveryservice.entities;

import com.covid.support.deliveryservice.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mobile_no")
    String mobileNo;

    @Column(name = "name")
    String name;

    @Column(name = "address")
    String address;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    UserType userType;

    @Column(name = "lat")
    Double lat;

    @Column(name = "lon")
    Double lon;

    @Column(name = "token")
    String token;
}
