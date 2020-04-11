package com.example.project.ecommerce.repos;

import com.example.project.ecommerce.model.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationRepo extends JpaRepository<ProductVariation,Long> {
}
