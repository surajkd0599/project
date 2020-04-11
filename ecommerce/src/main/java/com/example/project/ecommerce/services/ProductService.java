package com.example.project.ecommerce.services;

import com.example.project.ecommerce.model.ProductCategory;
import com.example.project.ecommerce.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public String addCategory(ProductCategory productCategory){ productRepository.save(productCategory);
    return "Product added.";}

    public List<Object[]> getCategory(){
       return productRepository.findCategory();
    }

    public List<Object[]> getProduct(String categoryName){
        Long categoryId = productRepository.categoryId(categoryName);
        return productRepository.findProduct(categoryId);
    }

    public List<Object[]> getVariation(String productName) {
        Long productId = productRepository.productId(productName);
        return productRepository.findVariation(productId);
    }

    public List<Object[]> priceLowToHigh(String productName){
        Long productId = productRepository.productId(productName);
        return productRepository.priceLowToHigh(productId);
    }

    public List<Object[]> priceHighToLow(String productName){
        Long productId = productRepository.productId(productName);
        return productRepository.priceHighToLow(productId);
    }

    public void updateStockByAdmin(String productName,String productSize,int quantity){
       List<Object[]> productVariations = getVariation(productName);
       Long variationId = 0L;
       for(Object[] pv : productVariations){
           if(String.valueOf(pv[4]) == productSize){
               variationId = (Long) pv[0];
               System.out.println(variationId);
           }
       }
        System.out.println(variationId);
       productRepository.updateStockByAdmin(variationId,quantity);
    }
}
