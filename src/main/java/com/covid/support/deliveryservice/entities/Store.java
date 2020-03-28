package com.covid.support.deliveryservice.entities;

import com.covid.support.deliveryservice.utils.StoreTypesConvert;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "store")
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mobile_no")
    String mobileNo;

    @Column(name = "address")
    String address;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "name")
    String name;

    @Column(name = "lat")
    Double lat;

    @Column(name = "lon")
    Double lon;

    @Column(name = "types")
    @Convert(converter = StoreTypesConvert.class)
    List<String> types;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "operational_start_time")
    Timestamp operationalStartTime;

    @Column(name = "operational_end_time")
    Timestamp operationalEndTime;

    @Column(name = "max_slots")
    Integer maxSlots;

    @Column(name = "slots")
    Integer slots;
}
