package com.ttn.bootcamp.project.ecommerce.models;

import javax.persistence.*;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long itemId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "variation_Id")
    //private List<ProductVariation> productVariation;
    private ProductVariation productVariation;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }
}
