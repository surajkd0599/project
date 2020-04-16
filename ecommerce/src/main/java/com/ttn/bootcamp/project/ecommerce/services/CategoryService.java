package com.ttn.bootcamp.project.ecommerce.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ttn.bootcamp.project.ecommerce.dtos.MetaDataFieldDto;
import com.ttn.bootcamp.project.ecommerce.dtos.MetaDataFieldValueDto;
import com.ttn.bootcamp.project.ecommerce.dtos.ProductCategoryDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.*;
import com.ttn.bootcamp.project.ecommerce.repos.*;
import com.ttn.bootcamp.project.ecommerce.utils.Utility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

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
        Category category = categoryRepo.findByCategoryName(productCategoryDto.getCategoryName());
        if (category != null) {
            sb.append("Category name already exist");
        } else {
            Category category1 = new Category();
            BeanUtils.copyProperties(productCategoryDto, category1);

            categoryRepo.save(category1);

            sb.append("Category field added and category id is : ").append(category1.getId());
        }
        return sb.toString();
    }

    public Category getCategoryById(Long categoryId) {

        Optional<Category> productCategory = categoryRepo.findById(categoryId);

        if (productCategory.isPresent()) {
            return productCategory.get();
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    public Set<ProductCategoryDto> getCategory() {
       List<Category> categories = categoryRepo.findAll();

        Set<ProductCategoryDto> productCategoryDtos = new HashSet<>();

        for(Category category : categories){
            ProductCategoryDto productCategoryDto = new ProductCategoryDto();
            BeanUtils.copyProperties(category,productCategoryDto);
            productCategoryDtos.add(productCategoryDto);
        }
        return productCategoryDtos;
    }

    public String updateCategory(Long categoryId, ProductCategoryDto productCategoryDto) {

        Optional<Category> productCategory = categoryRepo.findById(categoryId);

        Category category1 = categoryRepo.findByCategoryName(productCategoryDto.getCategoryName());
        if (productCategory.isPresent()) {
            productCategory.get().setCategoryName(productCategoryDto.getCategoryName());
            categoryRepo.save(productCategory.get());
        } else {
            throw new NotFoundException("Product category not found");
        }

        return "Category added";
    }

    public String addValue(Long categoryId, List<MetaDataFieldValueDto> metaDataFieldValueDtos) {

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

    public String updateValue(MetaDataFieldValueDto metaDataFieldValueDto) {

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

    //List of categories
    public List<CategoryMetaDataFieldValues> getCategories() {
        return categoryMetaDataFieldValuesRepo.findAll();
    }

    /*public List<CategoryMetaDataFieldValues> getCategories() {
        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepo.findAll();

        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues1 = Arrays.asList(c)

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("product");

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("Value-Filter",filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(categoryMetaDataFieldValues);
        mappingJacksonValue.setFilters(filterProvider);
        return (List<CategoryMetaDataFieldValues>) mappingJacksonValue;
    }*/
}
