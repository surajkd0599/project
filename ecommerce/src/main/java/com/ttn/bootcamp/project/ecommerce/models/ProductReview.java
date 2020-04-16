package com.ttn.bootcamp.project.ecommerce.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String comment;
    @Temporal(TemporalType.DATE)
    private Date reviewCreatedAt;
    @Temporal(TemporalType.DATE)
    private Date reviewModifiedAt;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "customer_user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewCreatedAt() {
        return reviewCreatedAt;
    }

    public void setReviewCreatedAt(Date reviewCreatedAt) {
        this.reviewCreatedAt = reviewCreatedAt;
    }

    public Date getReviewModifiedAt() {
        return reviewModifiedAt;
    }

    public void setReviewModifiedAt(Date reviewModifiedAt) {
        this.reviewModifiedAt = reviewModifiedAt;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
