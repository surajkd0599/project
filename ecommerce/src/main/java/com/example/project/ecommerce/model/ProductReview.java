package com.example.project.ecommerce.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long reviewId;
    private String comment;
    @Temporal(TemporalType.DATE)
    private Date reviewCreatedAt;
    @Temporal(TemporalType.DATE)
    private Date reviewModifiedAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
