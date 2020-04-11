package com.example.project.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.runtime.JSONFunctions;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hibernate.annotations.Type;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long variationId;
    private String color;
    private String size;
    private Long price;
    private String gender;
    private JSONObject metadata;
    private int stock;
    private String brand;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductReview> review;

    public Long getVariationId() {
        return variationId;
    }

    public void setVariationId(Long variationId) {
        this.variationId = variationId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductVariation() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<ProductReview> getReview() {
        return review;
    }

    public void setReview(List<ProductReview> review) {
        this.review = review;
    }

    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }
}
