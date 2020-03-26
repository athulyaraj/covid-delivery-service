package com.covid.support.deliveryservice.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Column(name = "created_at")
    Timestamp createdAt;

    @Column(name = "updated_at")
    Timestamp updatedAt;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "updated_by")
    String updatedBy;
}
