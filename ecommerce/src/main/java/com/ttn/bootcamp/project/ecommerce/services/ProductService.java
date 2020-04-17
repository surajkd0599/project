package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.Category;
import com.ttn.bootcamp.project.ecommerce.models.Product;
import com.ttn.bootcamp.project.ecommerce.models.ProductVariation;
import com.ttn.bootcamp.project.ecommerce.models.Seller;
import com.ttn.bootcamp.project.ecommerce.repos.CategoryRepo;
import com.ttn.bootcamp.project.ecommerce.repos.ProductRepo;
import com.ttn.bootcamp.project.ecommerce.repos.ProductVariationRepo;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private ProductVariationRepo productVariationRepo;

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
                product.setDeleted(false);

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

    public String addProductVariation(Long productId, ProductVariationDto productVariationDto){

        Optional<Product> product = productRepo.findById(productId);

        if(product.isPresent()){
            if(productVariationDto.getQuantity()>0){
                if (productVariationDto.getPrice()>0){
                    if(product.get().isActive() && !(product.get().isDeleted())){

                        ProductVariation productVariation = new ProductVariation();
                        BeanUtils.copyProperties(productVariationDto,productVariation);
                        productVariation.setProduct(product.get());

                        /////////////// Image is to be added
                        productVariationRepo.save(productVariation);

                    }else {
                        throw new BadRequestException("Product may be deleted or inactive");
                    }
                }else {
                    throw new BadRequestException("Price should be greater than 0");
                }

            }else {
                throw new BadRequestException("Quantity should be greater than 0");
            }
        }else {
            throw new NotFoundException("Product not found");
        }
        return "Variation saved";
    }

    public ProductViewDto getProduct(Long userId, Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {
                if (!(product.get().isDeleted())) {
                    //return product.get();
                    ProductViewDto productViewDto = new ProductViewDto();
                    BeanUtils.copyProperties(product.get(), productViewDto);

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

    public ProductVariationGetDto getProductVariation(Long userId, Long variationId){

        Optional<ProductVariation> productVariation = productVariationRepo.findById(variationId);

        if(productVariation.isPresent()){
            if(userId.equals(productVariation.get().getProduct().getSeller().getId())){
                if(!(productVariation.get().getProduct().isDeleted())){

                    ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                    BeanUtils.copyProperties(productVariation.get(),productVariationGetDto);
                    productVariationGetDto.setProductId(productVariation.get().getProduct().getId());
                    productVariationGetDto.setProductName(productVariation.get().getProduct().getProductName());

                    return productVariationGetDto;

                }else {
                    throw new BadRequestException("Product is deleted");
                }
            }else {
                throw new BadRequestException("You don't have authorization to view this product");
            }

        }else {
            throw new NotFoundException("Product Variation not found with variaton id : "+variationId);
        }

    }

    public Set<ProductViewDto> getProducts(Long userId) {
        List<Product> products = productRepo.findAllProducts(userId);

        Set<ProductViewDto> productViewDtos = new HashSet<>();

            for (Product product : products) {
                ProductViewDto productViewDto = new ProductViewDto();

                if (!(product.isDeleted())) {
                    BeanUtils.copyProperties(product, productViewDto);

                    productViewDto.setCompanyName(product.getSeller().getCompanyName());

                    productViewDtos.add(productViewDto);
                }
            }
            if (productViewDtos.size() < 1) {
                throw new BadRequestException(" or product may be deleted");
            }
            return productViewDtos;
    }

    public List<ProductVariationGetDto> getProductVariations(Long userId, Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if(product.isPresent()){
            if(userId.equals(product.get().getSeller().getId())){
                if(!(product.get().isDeleted())){
                    List<ProductVariation> productVariations = productVariationRepo.findByProductId(productId);

                    List<ProductVariationGetDto> productVariationGetDtos  = new ArrayList<>();

                    for(ProductVariation productVariation1 : productVariations ){
                        ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                        BeanUtils.copyProperties(productVariation1,productVariationGetDto);
                        productVariationGetDto.setProductId(productVariation1.getProduct().getId());
                        productVariationGetDto.setProductName(productVariation1.getProduct().getProductName());

                        productVariationGetDtos.add(productVariationGetDto);
                    }

                    return productVariationGetDtos;

                }else {
                    throw new BadRequestException("Product may be deleted");
                }
            }else{
                throw new BadRequestException("You don't have authorization to view this product");
            }
        }else {
            throw new NotFoundException("Product not found for product id : "+productId);
        }
    }

    public String deleteProduct(Long userId, Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {
                product.get().setDeleted(true);
                productRepo.save(product.get());

                return "Product deleted";
            } else {
                throw new BadRequestException("You don't have authorization to delete this product");
            }
        } else {
            throw new NotFoundException("Product not found");
        }

    }

    public String updateProduct(Long userId, Long productId, ProductViewDto productViewDto) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {
                product.get().setDescription(productViewDto.getDescription());
                product.get().setReturnable(productViewDto.isReturnable());
                product.get().setCancellable(productViewDto.isCancellable());
                product.get().setDeleted(false);

                productRepo.save(product.get());

                return "Product updated successfully";
            } else {
                throw new BadRequestException("You don't have authorization to update this product");
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    public String updateProductName(Long userId, Long categoryId, Long productId,  ProductViewDto productViewDto) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {

                Long productId1 = productRepo.findUniqueProduct(productViewDto.getBrand(), categoryId, userId, productViewDto.getProductName());

                if(productId1 == null) {
                    product.get().setProductName(productViewDto.getProductName());
                    productRepo.save(product.get());
                    return "Product updated successfully";
                }else {
                    throw new BadRequestException("Product name must be unique");
                }
            } else {
                throw new BadRequestException("You don't have authorization to update this product");
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    public String updateProductVariation(Long userId,Long variationId, ProductVariationDto productVariationDto){

        Optional<ProductVariation> productVariation = productVariationRepo.findById(variationId);

        if(productVariation.isPresent()){
            if (userId.equals(productVariation.get().getProduct().getSeller().getId())){
                if(!(productVariation.get().getProduct().isDeleted()) && productVariation.get().getProduct().isActive()){

                    productVariation.get().setQuantity(productVariationDto.getQuantity());
                    productVariation.get().setActive(productVariationDto.isActive());
                    productVariation.get().setPrice(productVariationDto.getPrice());

                    //////////// Image is to be added

                    productVariationRepo.save(productVariation.get());

                    return "Product variation updated successfully";
                }else {
                    throw new BadRequestException("Product may be deleted or inactive");
                }

            }else {
                throw new BadRequestException("You don't have authorization to update this product variation");
            }

        }else {
            throw new NotFoundException("Product variation not found for variation id : "+variationId);
        }

    }

    public List<ProductVariationGetDto> getProductForAdmin(Long productId){

        Optional<Product> product = productRepo.findById(productId);

        if(product.isPresent()){

                List<ProductVariation> productVariations = productVariationRepo.findByProductId(productId);

                if(productVariations.size()>0){
                    List<ProductVariationGetDto> productVariationGetDtos  = new ArrayList<>();

                    for(ProductVariation productVariation1 : productVariations ){
                        ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                        BeanUtils.copyProperties(productVariation1,productVariationGetDto);
                        productVariationGetDto.setProductId(productVariation1.getProduct().getId());
                        productVariationGetDto.setProductName(productVariation1.getProduct().getProductName());

                        productVariationGetDtos.add(productVariationGetDto);
                    }

                    return productVariationGetDtos;
                }

        }else {
            throw new NotFoundException("Product not found");
        }
        return null;
    }

    public List<ProductVariationGetDto> getProductForUser(Long productId){

        Optional<Product> product = productRepo.findById(productId);

        if(product.isPresent()){
            if (product.get().isActive() && !(product.get().isDeleted())){

                List<ProductVariation> productVariations = productVariationRepo.findByProductId(productId);

                if(productVariations.size()>0){
                    List<ProductVariationGetDto> productVariationGetDtos  = new ArrayList<>();

                    for(ProductVariation productVariation1 : productVariations ){
                        ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                        BeanUtils.copyProperties(productVariation1,productVariationGetDto);
                        productVariationGetDto.setProductId(productVariation1.getProduct().getId());
                        productVariationGetDto.setProductName(productVariation1.getProduct().getProductName());

                        productVariationGetDtos.add(productVariationGetDto);
                    }

                    return productVariationGetDtos;
                }

            }else {
                throw new BadRequestException("Product may be deleted or inactive");
            }

        }else {
            throw new NotFoundException("Product not found");
        }
        return null;
    }

    public AllProductDto getAllProductsByCategoryId(Long categoryId){

        Optional<Category> category = categoryRepo.findById(categoryId);

        if(category.isPresent()){

            List<Product> products = productRepo.findAllProduct(categoryId);

            List<Product> productList = new ArrayList<>();

            for(Product product : products){
                if(product.isActive() && !(product.isDeleted())) {
                    if (product.getProductVariations().size() > 0) {
                        productList.add(product);
                    }
                }
            }

            AllProductDto allProductDto = new AllProductDto();

            allProductDto.setCategoryId(categoryId);
            allProductDto.setProductList(productList);

            return allProductDto;

        }else {
            throw new NotFoundException("Category not found for this Category Id");
        }

    }

    public String deActivateProduct(Long productId){

        Optional<Product> product = productRepo.findById(productId);

        if(product.get().isActive()){
            product.get().setActive(false);
            productRepo.save(product.get());

            sendEmail.sendEmail("Product De-Activation", "Product name is : " + product.get().getProductName() + " and product id is : " + product.get().getId(), product.get().getSeller().getEmail());
            return "Product deActivated";
        }else {
            throw new BadRequestException("Product is already deactivated");
        }
    }

    public String activateProduct(Long productId){

        Optional<Product> product = productRepo.findById(productId);

        if(!product.get().isActive()){
            product.get().setActive(true);
            productRepo.save(product.get());

            sendEmail.sendEmail("Product Activation", "Product name is : " + product.get().getProductName() + " and product id is : " + product.get().getId(), product.get().getSeller().getEmail());
            return "Product Activated";
        }else {
            throw new BadRequestException("Product is already activated");
        }
    }

}
