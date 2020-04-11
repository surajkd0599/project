package com.example.project.ecommerce.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderId;
    @Temporal(TemporalType.DATE)
    private Date orderPlacedDate;
    @Temporal(TemporalType.DATE)
    private Date orderReceivedDate;
    private String orderStatus;
    private int orderPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id",referencedColumnName = "orderId")
    private List<OrderItem> orderItem;
}
