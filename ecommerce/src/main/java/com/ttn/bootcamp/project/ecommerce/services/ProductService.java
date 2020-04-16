package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.models.ProductCategory;
import com.ttn.bootcamp.project.ecommerce.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public String addCategory(ProductCategory productCategory){ productRepo.save(productCategory);
    return "Product added.";}

   /* public List<Object[]> getCategory(){
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
    }*/
}
