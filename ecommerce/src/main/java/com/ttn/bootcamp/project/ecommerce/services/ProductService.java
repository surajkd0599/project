package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.dtos.ProductDto;
import com.ttn.bootcamp.project.ecommerce.dtos.ProductViewDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Category;
import com.ttn.bootcamp.project.ecommerce.models.Product;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.repos.CategoryRepo;
import com.ttn.bootcamp.project.ecommerce.repos.ProductRepo;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private SendEmail sendEmail;

    public String addProduct(Long sellerId, Long categoryId, ProductDto productDto) {

        Optional<Category> category = categoryRepo.findById(categoryId);

        Optional<Seller> seller = sellerRepo.findById(sellerId);

        if (category.isPresent()) {

            Long productId = productRepo.findUniqueProduct(productDto.getBrand(), categoryId, sellerId, productDto.getProductName());

            if (productId == null) {
                Product product = new Product();

                product.setBrand(productDto.getBrand());
                product.setCategory(category.get());
                product.setSeller(seller.get());
                product.setProductName(productDto.getProductName());
                product.setDescription(productDto.getDescription());
                product.setDeleted(true);

                productRepo.save(product);

                sendEmail.sendEmail("Activate Product", "Product name is : " + productDto.getProductName() + " and seller is : " + seller.get().getCompanyName() + " and product id is : " + product.getId(), "sd9808006@gmail.com");

                return "Product added successfully";
            } else {
                throw new BadRequestException("Product name already exist");
            }

        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public ProductViewDto viewProduct(Long userId, Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {
                if (product.get().isDeleted()) {
                    //return product.get();
                    ProductViewDto productViewDto = new ProductViewDto();
                    BeanUtils.copyProperties(product.get(),productViewDto);

                    return productViewDto;
                } else {
                    throw new BadRequestException("Product is Deleted");
                }

            } else {
                throw new BadRequestException("You don't have authorization to view this product");
            }
        } else {
            throw new NotFoundException("Product not found");
        }

    }


    public Set<ProductViewDto> getProducts(Long userId){
        List<Product> products = productRepo.findAllProducts(userId);

        Set<ProductViewDto> productViewDtos = new HashSet<>();

        for(Product product : products){
            ProductViewDto productViewDto = new ProductViewDto();

            BeanUtils.copyProperties(product,productViewDto);

            productViewDtos.add(productViewDto);
        }

        return productViewDtos;
    }

}
