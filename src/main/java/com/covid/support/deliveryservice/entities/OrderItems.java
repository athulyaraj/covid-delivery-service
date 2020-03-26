package com.covid.support.deliveryservice.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "order_items")
public class OrderItems extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Column(name = "item_name")
    String itemName;

    @Column(name = "quantity")
    Double quantity;

    @Column(name = "unit")
    String unit;

    @Column(name = "availability_status")
    boolean availabilityStatus;

    @Column(name = "image_url")
    String imageUrl;

}
