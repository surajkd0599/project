package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = "select p.id from product p where p.brand=:brand AND p.category_id=:categoryId AND p.seller_user_id=:sellerId AND p.product_name=:name",nativeQuery = true)
    Long findUniqueProduct(@Param("brand")  String brand, @Param("categoryId") Long categoryId,@Param("sellerId") Long sellerId, @Param("name") String name);

    @Query(value = "select * from product where seller_user_id=:userId AND is_deleted=true",nativeQuery = true)
    List<Product> findAllProducts(@Param("userId") Long userId);

    @Query(value = "select * from product where category_id=:categoryId",nativeQuery = true)
    List<Product> findAllProduct(@Param("categoryId") Long categoryId);

    /*ProductCategory save(ProductCategory productCategory);

    Optional<ProductCategory> findById(Long id);

    @Query("from ProductCategory pc where pc.categoryName=:name")
    List<Object[]> findByName(@Param("name") String name);

    @Query("Select categoryName from ProductCategory")
    List<Object[]> findCategory();

    //Query for categoryId
    @Query("Select categoryId from ProductCategory where categoryName=:categoryName")
    Long categoryId(@Param("categoryName") String categoryName);
    //Query for productId
    @Query("Select productId from Product where productName=:productName")
    Long productId(@Param("productName") String productName);

    //Query to find Product
    @Query(value = "Select product_name from product where category_id=:categoryId",nativeQuery = true)
    List<Object[]> findProduct(@Param("categoryId") Long categoryId);

    //Query to find variation

    @Query(value = "Select * from product_variation where product_id=:productId",nativeQuery = true)
    List<Object[]> findVariation(@Param("productId") Long productId);

    //Query to find low to high based on price

    @Query(value = "Select * from product_variation where product_id=:productId ORDER BY price ASC "
            ,nativeQuery = true)
    List<Object[]> priceLowToHigh(@Param("productId") Long productId);

    //Query to find high to low based on price
    @Query(value = "Select * from product_variation where product_id=:productId ORDER BY price DESC "
            ,nativeQuery = true)
    List<Object[]> priceHighToLow(@Param("productId") Long productId);

    //Query to update product stock by admin
    @Transactional
    @Modifying
    @Query("Update ProductVariation p Set p.stock =:quantity where p.variationId=:variationId")
    int updateStockByAdmin(@Param("variationId") Long variationId,@Param("quantity") int quantity);*/
}
