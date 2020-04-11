package com.example.project.ecommerce.repos;

import com.example.project.ecommerce.model.Product;
import com.example.project.ecommerce.model.ProductCategory;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductCategory, Long> {

    ProductCategory save(ProductCategory productCategory);

    Product findByCategoryId(Long categoryid);

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
    int updateStockByAdmin(@Param("variationId") Long variationId,@Param("quantity") int quantity);
}
