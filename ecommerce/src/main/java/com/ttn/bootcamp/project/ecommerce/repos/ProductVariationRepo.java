package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.ProductVariation;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ProductVariationRepo extends JpaRepository<ProductVariation, Long> {

    @Query(value = "Select * from product_variation where product_id=:productId", nativeQuery = true)
    List<ProductVariation> findByProductId(@Param("productId") Long productId);

    @Query("Select metadata from ProductVariation where id=:variationId")
    JSONObject findMetadata(@Param("variationId") Long variationId);
}
