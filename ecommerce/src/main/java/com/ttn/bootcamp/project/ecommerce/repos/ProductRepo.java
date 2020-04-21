package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.dtos.ProductMinMaxPriceDto;
import com.ttn.bootcamp.project.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = "select p.id from product p where p.brand=:brand AND p.category_id=:categoryId AND p.seller_user_id=:sellerId AND p.product_name=:name", nativeQuery = true)
    Long findUniqueProduct(@Param("brand") String brand, @Param("categoryId") Long categoryId, @Param("sellerId") Long sellerId, @Param("name") String name);

    @Query(value = "select * from product where seller_user_id=:userId AND is_deleted=false", nativeQuery = true)
    List<Product> findAllProducts(@Param("userId") Long userId);

    @Query(value = "select * from product where category_id=:categoryId", nativeQuery = true)
    List<Product> findAllProduct(@Param("categoryId") Long categoryId);

    @Query(value = "select min(pv.price) as minPrice,max(pv.price) as maxPrice from product p inner join product_variation pv on pv.product_id=p.id where p.category_id=:categoryId group by p.category_id", nativeQuery = true)
    ProductMinMaxPriceDto findMinMaxPriceBasedOnCategory(@Param("categoryId") Long categoryId);

    @Query(value = "select * from product p where p.category_id=:categoryId or p.product_name=:productName or p.id in (select pv.product_id from product_variation pv)", nativeQuery = true)
    List<Product> findSimilarProducts(@Param(value = "categoryId") Long categoryId, @Param(value = "productName") String productName);
}
