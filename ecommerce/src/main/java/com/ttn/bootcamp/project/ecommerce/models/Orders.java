package com.ttn.bootcamp.project.ecommerce.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date orderPlacedDate;
    @Temporal(TemporalType.DATE)
    private Date orderReceivedDate;
    private String orderStatus;
    private int orderPrice;

    @Column(name = "customer_user_id")
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private List<OrderItem> orderItem;
}
