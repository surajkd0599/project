package com.ttn.bootcamp.project.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.*;
import com.ttn.bootcamp.project.ecommerce.repos.CategoryRepo;
import com.ttn.bootcamp.project.ecommerce.repos.ProductRepo;
import com.ttn.bootcamp.project.ecommerce.repos.ProductVariationRepo;
import com.ttn.bootcamp.project.ecommerce.repos.SellerRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Autowired
    private ObjectMapper objectMapper;

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
                product.setDateCreated(new Date());

                productRepo.save(product);

                sendEmail.sendEmail("Activate Product", "Product name is : " + productDto.getProductName() + " and seller is : " + seller.get().getCompanyName() + " and product id is : " + product.getId(), "suraj.dubey@tothenew.com");

                return "Product added successfully";
            } else {
                throw new BadRequestException("Product name already exist");
            }

        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public String addProductVariation(Long productId, ProductVariationDto productVariationDto) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (productVariationDto.getQuantity() > 0) {
                if (productVariationDto.getPrice() > 0) {
                    if (product.get().isActive() && !(product.get().isDeleted())) {
                        if (!isMetaDataFieldValueSame(productVariationDto)) {
                            throw new BadRequestException("Product variation metadata not exist");
                        }
                        ProductVariation productVariation = new ProductVariation();
                        BeanUtils.copyProperties(productVariationDto, productVariation);
                        productVariation.setProduct(product.get());
                        productVariation.setDateCreated(new Date());
                        productVariationRepo.save(productVariation);
                    } else {
                        throw new BadRequestException("Product may be deleted or inactive");
                    }
                } else {
                    throw new BadRequestException("Price should be greater than 0");
                }

            } else {
                throw new BadRequestException("Quantity should be greater than 0");
            }
        } else {
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

    public ProductVariationGetDto getProductVariation(Long userId, Long variationId) {

        Optional<ProductVariation> productVariation = productVariationRepo.findById(variationId);

        if (productVariation.isPresent()) {
            if (userId.equals(productVariation.get().getProduct().getSeller().getId())) {
                if (!(productVariation.get().getProduct().isDeleted())) {

                    ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                    BeanUtils.copyProperties(productVariation.get(), productVariationGetDto);
                    productVariationGetDto.setProductId(productVariation.get().getProduct().getId());
                    productVariationGetDto.setProductName(productVariation.get().getProduct().getProductName());

                    return productVariationGetDto;

                } else {
                    throw new BadRequestException("Product is deleted");
                }
            } else {
                throw new BadRequestException("You don't have authorization to view this product");
            }

        } else {
            throw new NotFoundException("Product Variation not found with variaton id : " + variationId);
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
            throw new BadRequestException(" You have not authorization to view this product or product may be deleted");
        }
        return productViewDtos;
    }

    public List<ProductVariationGetDto> getProductVariations(Long userId, Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {
                if (!(product.get().isDeleted())) {
                    List<ProductVariation> productVariations = productVariationRepo.findByProductId(productId);

                    List<ProductVariationGetDto> productVariationGetDtos = new ArrayList<>();

                    for (ProductVariation productVariation1 : productVariations) {
                        ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                        BeanUtils.copyProperties(productVariation1, productVariationGetDto);
                        productVariationGetDto.setProductId(productVariation1.getProduct().getId());
                        productVariationGetDto.setProductName(productVariation1.getProduct().getProductName());

                        productVariationGetDtos.add(productVariationGetDto);
                    }

                    return productVariationGetDtos;

                } else {
                    throw new BadRequestException("Product may be deleted");
                }
            } else {
                throw new BadRequestException("You don't have authorization to view this product");
            }
        } else {
            throw new NotFoundException("Product not found for product id : " + productId);
        }
    }

    @Transactional
    @Modifying
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

    @Transactional
    @Modifying
    public String updateProduct(Long userId, Long productId, ProductViewDto productViewDto) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {
                product.get().setDescription(productViewDto.getDescription());
                product.get().setReturnable(productViewDto.isReturnable());
                product.get().setCancellable(productViewDto.isCancellable());
                product.get().setDeleted(productViewDto.isDeleted());

                productRepo.save(product.get());

                return "Product updated successfully";
            } else {
                throw new BadRequestException("You don't have authorization to update this product");
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @Transactional
    @Modifying
    public String updateProductName(Long userId, Long categoryId, Long productId, ProductViewDto productViewDto) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (userId.equals(product.get().getSeller().getId())) {

                Long productId1 = productRepo.findUniqueProduct(productViewDto.getBrand(), categoryId, userId, productViewDto.getProductName());

                if (productId1 == null) {
                    product.get().setProductName(productViewDto.getProductName());
                    productRepo.save(product.get());
                    return "Product updated successfully";
                } else {
                    throw new BadRequestException("Product name must be unique");
                }
            } else {
                throw new BadRequestException("You don't have authorization to update this product");
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @Transactional
    @Modifying
    public String updateProductVariation(Long userId, Long variationId, ProductVariationDto productVariationDto) {

        Optional<ProductVariation> productVariation = productVariationRepo.findById(variationId);

        if (productVariation.isPresent()) {
            if (userId.equals(productVariation.get().getProduct().getSeller().getId())) {
                if (!(productVariation.get().getProduct().isDeleted()) && productVariation.get().getProduct().isActive()) {

                    productVariation.get().setQuantity(productVariationDto.getQuantity());
                    productVariation.get().setActive(productVariationDto.isActive());
                    productVariation.get().setPrice(productVariationDto.getPrice());

                    //////////// Image is to be added

                    productVariationRepo.save(productVariation.get());

                    return "Product variation updated successfully";
                } else {
                    throw new BadRequestException("Product may be deleted or inactive");
                }

            } else {
                throw new BadRequestException("You don't have authorization to update this product variation");
            }

        } else {
            throw new NotFoundException("Product variation not found for variation id : " + variationId);
        }

    }

    public List<ProductVariationGetDto> getProductForAdmin(Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {

            List<ProductVariation> productVariations = productVariationRepo.findByProductId(productId);

            if (productVariations.size() > 0) {
                List<ProductVariationGetDto> productVariationGetDtos = new ArrayList<>();

                for (ProductVariation productVariation1 : productVariations) {
                    ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                    BeanUtils.copyProperties(productVariation1, productVariationGetDto);
                    productVariationGetDto.setProductId(productVariation1.getProduct().getId());
                    productVariationGetDto.setProductName(productVariation1.getProduct().getProductName());
                    productVariationGetDto.setPrimaryImage("To see primary image click on :http://localhost:8080/ecommerce/image/variation/" + productVariation1.getId());

                    productVariationGetDtos.add(productVariationGetDto);
                }

                return productVariationGetDtos;
            }

        } else {
            throw new NotFoundException("Product not found");
        }
        return null;
    }

    public List<ProductVariationGetDto> getProductForUser(Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (product.get().isActive() && !(product.get().isDeleted())) {

                List<ProductVariation> productVariations = productVariationRepo.findByProductId(productId);

                if (productVariations.size() > 0) {
                    List<ProductVariationGetDto> productVariationGetDtos = new ArrayList<>();

                    for (ProductVariation productVariation1 : productVariations) {
                        ProductVariationGetDto productVariationGetDto = new ProductVariationGetDto();

                        BeanUtils.copyProperties(productVariation1, productVariationGetDto);
                        productVariationGetDto.setProductId(productVariation1.getProduct().getId());
                        productVariationGetDto.setProductName(productVariation1.getProduct().getProductName());

                        productVariationGetDtos.add(productVariationGetDto);
                    }

                    return productVariationGetDtos;
                }

            } else {
                throw new BadRequestException("Product may be deleted or inactive");
            }

        } else {
            throw new NotFoundException("Product not found");
        }
        return null;
    }

    public AllProductDto getAllProductsByCategoryId(Long categoryId) {

        Optional<Category> category = categoryRepo.findById(categoryId);

        if (category.isPresent()) {

            List<Product> products = productRepo.findAllProduct(categoryId);

            List<Product> productList = new ArrayList<>();

            for (Product product : products) {
                if (product.isActive() && !(product.isDeleted())) {
                    if (product.getProductVariations().size() > 0) {
                        productList.add(product);
                    }
                }
            }

            AllProductDto allProductDto = new AllProductDto();

            allProductDto.setCategoryId(categoryId);
            allProductDto.setProductList(productList);

            return allProductDto;

        } else {
            throw new NotFoundException("Category not found for this Category Id");
        }

    }

    @Transactional
    public String deActivateProduct(Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (product.get().isActive()) {
                product.get().setActive(false);
                productRepo.save(product.get());

                sendEmail.sendEmail("Product De-Activation", "Product name is : " + product.get().getProductName() + " and product id is : " + product.get().getId(), product.get().getSeller().getEmail());
                return "Product deActivated";
            } else {
                throw new BadRequestException("Product is already deactivated");
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @Transactional
    public String activateProduct(Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {
            if (!product.get().isActive()) {
                product.get().setActive(true);
                productRepo.save(product.get());

                sendEmail.sendEmail("Product Activation", "Product name is : " + product.get().getProductName() + " and product id is : " + product.get().getId(), product.get().getSeller().getEmail());
                return "Product Activated";
            } else {
                throw new BadRequestException("Product is already activated");
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    public List<Product> getSimilarProducts(Long productId) {

        Optional<Product> product = productRepo.findById(productId);

        if (product.isPresent()) {

            List<Product> products = productRepo.findSimilarProducts(product.get().getCategory().getId(), product.get().getProductName());

            return products;
        } else {
            throw new NotFoundException("Product not found for product id : " + productId);
        }

    }

    //to validate
    boolean isMetaDataFieldValueSame(ProductVariationDto productVariationDto) {
        boolean isValid = false;
        String inputValue;
        CategoryFieldValueDto categoryFieldValueDto = new CategoryFieldValueDto();
        CategoryFieldValueDto categoryFieldValueDto1 = new CategoryFieldValueDto();
        try {
            inputValue = objectMapper.writeValueAsString(productVariationDto.getMetadata());
            categoryFieldValueDto = objectMapper.readValue(inputValue, CategoryFieldValueDto.class);
            if (categoryFieldValueDto.getFieldValueMap().size() == 0) {
                throw new BadRequestException("Atleast one field value map should be in metadata");
            }
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
        }
        Optional<Category> category = categoryRepo.findById(categoryFieldValueDto.getCategoryId());
        if (category.isPresent()) {
            categoryFieldValueDto1 = getFieldValueByCategoryId(category.get().getId());
        }

        if (categoryFieldValueDto.getFieldValueMap().size() == categoryFieldValueDto1.getFieldValueMap().size()) {
            if (categoryFieldValueDto.equals(categoryFieldValueDto1)) {
                isValid = true;
            } else {
                isValid = false;
            }
        } else if ((isFieldValueMapSame(categoryFieldValueDto.getFieldValueMap(), categoryFieldValueDto1.getFieldValueMap()))) {
            isValid = true;
        }
        return isValid;
    }

    boolean isFieldValueMapSame(Map<String, String> fieldValueMap, Map<String, String> fieldValueMap1) {
        boolean isValid = false;

        for (Map.Entry<String, String> map : fieldValueMap.entrySet()) {
            isValid = false;
            String mapKey = map.getKey();
            String mapValue = map.getValue();
            for (Map.Entry<String, String> map1 : fieldValueMap1.entrySet()) {
                String map1Key = map1.getKey();
                String map1Value = map1.getValue();
                if (map1Key.equals(mapKey)) {
                    if (map1Value.equals(mapValue)) {
                        isValid = true;
                        break;
                    } else {
                        throw new BadRequestException("Values are not same the value for field: " + mapKey + " must be: " + map1Value + " but passed value is: " + mapValue);
                    }
                }
            }
        }
        return isValid;
    }

    CategoryFieldValueDto getFieldValueByCategoryId(Long categoryId) {
        Optional<Category> category = categoryRepo.findById(categoryId);
        CategoryFieldValueDto categoryFieldValueDto = new CategoryFieldValueDto();
        Map<String, String> fieldValueMap = new LinkedHashMap<>();
        if (category.isPresent()) {
            categoryFieldValueDto.setCategoryId(categoryId);
            categoryFieldValueDto.setCategoryName(category.get().getCategoryName());
            categoryFieldValueDto.setParentId(category.get().getParentId());

            Set<CategoryMetaDataFieldValues> set = category.get().getCategoryMetaDataFieldValues();
            for (CategoryMetaDataFieldValues categoryMetaDataFieldValues : set) {
                fieldValueMap.put(categoryMetaDataFieldValues.getCategoryMetaDataField().getName(),
                        categoryMetaDataFieldValues.getValue());
            }
            categoryFieldValueDto.setFieldValueMap(fieldValueMap);

        }
        return categoryFieldValueDto;
    }
}
