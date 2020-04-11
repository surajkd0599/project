package com.example.project.ecommerce.model;

import javax.persistence.*;
import java.util.List;

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
