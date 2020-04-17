package com.ttn.bootcamp.project.ecommerce.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ttn.bootcamp.project.ecommerce.dtos.*;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.*;
import com.ttn.bootcamp.project.ecommerce.repos.*;
import com.ttn.bootcamp.project.ecommerce.utils.Utility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryMetaDataFieldValuesRepo categoryMetaDataFieldValuesRepo;

    public String addMetaDataField(MetaDataFieldDto metaDataFieldDto) {

        StringBuilder sb = new StringBuilder();
        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepo.findByName(metaDataFieldDto.getName());

        if (categoryMetaDataField != null) {
            throw new BadRequestException("Field name already exist");
        } else {
            CategoryMetaDataField categoryMetaDataField1 = new CategoryMetaDataField();
            BeanUtils.copyProperties(metaDataFieldDto, categoryMetaDataField1);

            categoryMetaDataFieldRepo.save(categoryMetaDataField1);

            sb.append("MetaData field added and field id is : ").append(categoryMetaDataField1.getId());

            // String.format("MetaData field added and field id is : %L",categoryMetaDataField1.getId());
        }
        return sb.toString();
    }

    public MappingJacksonValue getMetaDataFields() {

        List<CategoryMetaDataField> categoryMetaDataFields = categoryMetaDataFieldRepo.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("MetaData-Filter", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(categoryMetaDataFields);

        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    public String addCategory(ProductCategoryDto productCategoryDto) {

        StringBuilder sb = new StringBuilder();

        if (productCategoryDto.getParentId() == 0) {
            Category category = categoryRepo.findExistingCategory(productCategoryDto.getCategoryName(), productCategoryDto.getParentId());

            if (null != category) {
                sb.append("Category name already exist");
            } else {
                Category category1 = new Category();
                BeanUtils.copyProperties(productCategoryDto, category1);

                categoryRepo.save(category1);

                sb.append("Category field added and category id is : ").append(category1.getId());
            }
        } else {

            Category category = categoryRepo.findExistingCategory1(productCategoryDto.getCategoryName(), productCategoryDto.getParentId());
            if (null != category) {
                throw new BadRequestException("Category name already exist");
            } else {

                Category category1 = categoryRepo.findExistingCategory(productCategoryDto.getCategoryName(), productCategoryDto.getParentId());

                if (null != category1) {
                    throw new BadRequestException("Category name already exist");
                } else {
                    Category category2 = new Category();
                    BeanUtils.copyProperties(productCategoryDto, category2);

                    categoryRepo.save(category2);

                    sb.append("Category field added and category id is : ").append(category2.getId());
                }
            }
        }
        return sb.toString();
    }

    public List<ProductCategoryDto> getCategoryById(Long categoryId) {

        List<Category> productCategory = categoryRepo.findExistingCategory(categoryId);

        List<ProductCategoryDto> productCategoryDtos = new ArrayList<>();

        if (null != productCategory) {
            for (Category category : productCategory) {
                ProductCategoryDto productCategoryDto = new ProductCategoryDto();

                BeanUtils.copyProperties(category, productCategoryDto);
                productCategoryDtos.add(productCategoryDto);
            }
            return productCategoryDtos;
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public Set<Set<ProductCategoryDto>> getCategory() {
        List<Category> categories = categoryRepo.findId();

        Set<Set<ProductCategoryDto>> productCategoryDtos = new HashSet<>();

        for (Category category : categories) {
            List<Category> productCategory = categoryRepo.findExistingCategory(category.getId());

            if (null != productCategory) {
                Set<ProductCategoryDto> productCategoryDtos1 = new HashSet<>();
                for (Category category1 : productCategory) {
                    ProductCategoryDto productCategoryDto = new ProductCategoryDto();
                    BeanUtils.copyProperties(category1, productCategoryDto);
                    productCategoryDtos1.add(productCategoryDto);
                }
                productCategoryDtos.add(productCategoryDtos1);
            } else {
                throw new NotFoundException("Category not found");
            }
        }
        return productCategoryDtos;
    }

    public String updateCategory(Long categoryId, ProductCategoryDto productCategoryDto) {

        StringBuilder sb = new StringBuilder();

        Optional<Category> existingCategory = categoryRepo.findById(categoryId);

        if (existingCategory.isPresent()) {
            if (existingCategory.get().getParentId() == 0) {
                Category category = categoryRepo.findExistingCategory(productCategoryDto.getCategoryName(), existingCategory.get().getParentId());

                if (null != category) {
                    sb.append("Category name already exist");
                } else {
                    existingCategory.get().setCategoryName(productCategoryDto.getCategoryName());
                    categoryRepo.save(existingCategory.get());

                    sb.append("Category updated successfully");
                }
            } else {

                Category category = categoryRepo.findExistingCategory1(productCategoryDto.getCategoryName(), existingCategory.get().getParentId());
                if (null != category) {
                    throw new BadRequestException("Category name already exist");
                } else {

                    Category category1 = categoryRepo.findExistingCategory(productCategoryDto.getCategoryName(), existingCategory.get().getParentId());

                    if (null != category1) {
                        throw new BadRequestException("Category name already exist");
                    } else {
                        existingCategory.get().setCategoryName(productCategoryDto.getCategoryName());

                        categoryRepo.save(existingCategory.get());

                        sb.append("Category updated successfully");
                    }
                }
            }
            return sb.toString();
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public String addMetaDataFieldValue(Long categoryId, List<MetaDataFieldValueDto> metaDataFieldValueDtos) {

        Optional<Category> category = categoryRepo.findById(categoryId);

        if (category.isPresent()) {
            for (MetaDataFieldValueDto dto : metaDataFieldValueDtos) {
                Optional<CategoryMetaDataField> categoryMetaDataField = categoryMetaDataFieldRepo.findById(dto.getFieldId());

                if (categoryMetaDataField.isPresent()) {
                    CategoryMetaDataFieldValues categoryMetaDataFieldValues = new CategoryMetaDataFieldValues();

                    Utility.checkDuplicates(dto.getValue());

                    categoryMetaDataFieldValues.setCategory(category.get());
                    categoryMetaDataFieldValues.setCategoryMetaDataField(categoryMetaDataField.get());
                    categoryMetaDataFieldValues.setValue(dto.getValue());

                    categoryMetaDataFieldValuesRepo.save(categoryMetaDataFieldValues);
                } else {
                    throw new NotFoundException("Field id does not exist");
                }

            }
        } else {
            throw new NotFoundException("Category id not exist");
        }

        return "Value added";
    }

    public String updateMetaDataFieldValue(MetaDataFieldValueDto metaDataFieldValueDto) {

        Optional<Category> productCategory = categoryRepo.findById(metaDataFieldValueDto.getCategoryId());

        if (productCategory.isPresent()) {

            Optional<CategoryMetaDataField> metaDataField = categoryMetaDataFieldRepo.findById(metaDataFieldValueDto.getFieldId());

            if (metaDataField.isPresent()) {

                Utility.checkDuplicates(metaDataFieldValueDto.getValue());
                Optional<CategoryMetaDataFieldValues> valuesExist = categoryMetaDataFieldValuesRepo.findById(metaDataFieldValueDto.getCategoryId());

                if (valuesExist.isPresent()) {
                    valuesExist.get().setValue(metaDataFieldValueDto.getValue());
                    categoryMetaDataFieldValuesRepo.save(valuesExist.get());
                } else {
                    throw new BadRequestException("Value does not exist");
                }
            } else {
                throw new NotFoundException("Field id does not exist");
            }
        } else {
            throw new NotFoundException("Category id not exist");
        }

        return "Value updated";
    }

    public CategoryFieldValueDto getFieldValueByCategoryId(Long categoryId) {
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


    public List<CategoryFieldValueDto> getCategories() {
        List<Category> category = categoryRepo.findAll();
        List<CategoryFieldValueDto> categoryFieldValueDtoList = new ArrayList<>();
        Map<String, String> fieldValueMap = new LinkedHashMap<>();
        if (category.size() > 0) {
            for (Category categoryInner : category) {
                    CategoryFieldValueDto categoryFieldValueDto = new CategoryFieldValueDto();
                    categoryFieldValueDto.setCategoryId(categoryInner.getId());
                    categoryFieldValueDto.setCategoryName(categoryInner.getCategoryName());
                    categoryFieldValueDto.setParentId(categoryInner.getParentId());
                    Set<CategoryMetaDataFieldValues> set = categoryInner.getCategoryMetaDataFieldValues();
                    for (CategoryMetaDataFieldValues categoryMetaDataFieldValues : set) {
                        fieldValueMap.put(categoryMetaDataFieldValues.getCategoryMetaDataField().getName(),
                                categoryMetaDataFieldValues.getValue());
                    }
                    categoryFieldValueDto.setFieldValueMap(fieldValueMap);
                    categoryFieldValueDtoList.add(categoryFieldValueDto);
            }
        }
        return categoryFieldValueDtoList;
    }

    //Api to fetch filtering details
    public CategoryFilterDto categoryFilter(Long categoryId) {

        CategoryFilterDto categoryFilterDto = new CategoryFilterDto();

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

            List<Product> products = productRepo.findAllProduct(categoryId);

            List<String> brands = new ArrayList<>();
            for(Product product : products){
                brands.add(product.getBrand());
            }
            ProductMinMaxPriceDto productMinMaxPriceDto=productRepo.findMinMaxPriceBasedOnCategory(categoryId);
            categoryFilterDto.setCategoryFieldValueDto(categoryFieldValueDto);
            categoryFilterDto.setBrands(brands);
            categoryFilterDto.setMinimumPrice(productMinMaxPriceDto.getMinPrice());
            categoryFilterDto.setMaximumPrice(productMinMaxPriceDto.getMaxPrice());
        }
        return categoryFilterDto;
    }
}
