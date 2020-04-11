package com.example.project.ecommerce.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int cartId;

    private int quantity;

    private Long totalAmount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id",referencedColumnName = "cartId")
    private Set<CartItem> cartItem;

    private String isWishlistItem;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(Set<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public String getIsWishlistItem() {
        return isWishlistItem;
    }

    public void setIsWishlistItem(String isWishlistItem) {
        this.isWishlistItem = isWishlistItem;
    }
}
