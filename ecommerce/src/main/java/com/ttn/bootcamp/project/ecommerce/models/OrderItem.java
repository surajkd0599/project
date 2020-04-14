package com.ttn.bootcamp.project.ecommerce.models;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long OrderItemId;
    private int quantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "variation_id")
    private ProductVariation productVariations;
}
