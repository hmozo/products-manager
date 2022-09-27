package com.doctorkernel.ordersservice.domain.projection;

import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "orders")
@Data
public class OrderEntity implements Serializable {

    @Id
    @Column(unique = true)
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
