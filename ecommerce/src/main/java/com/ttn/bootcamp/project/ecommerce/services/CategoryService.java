package com.ttn.bootcamp.project.ecommerce.services;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ttn.bootcamp.project.ecommerce.dtos.CategoryProjectionDto;
import com.ttn.bootcamp.project.ecommerce.dtos.MetaDataFieldDto;
import com.ttn.bootcamp.project.ecommerce.dtos.MetaDataFieldValueDto;
import com.ttn.bootcamp.project.ecommerce.dtos.ProductCategoryDto;
import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.UserNotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.*;
import com.ttn.bootcamp.project.ecommerce.repos.*;
import com.ttn.bootcamp.project.ecommerce.utils.Utility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    @Autowired
    private CategoryMetaDataFieldValuesRepo categoryMetaDataFieldValuesRepo;

    public String addMetaDataField(MetaDataFieldDto metaDataFieldDto){

        StringBuilder sb = new StringBuilder();
            CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepo.findByName(metaDataFieldDto.getName());

            if(categoryMetaDataField != null){
                throw new BadRequestException("Field name already exist");
            }else {
                CategoryMetaDataField categoryMetaDataField1 = new CategoryMetaDataField();
                BeanUtils.copyProperties(metaDataFieldDto, categoryMetaDataField1);

                categoryMetaDataFieldRepo.save(categoryMetaDataField1);

                sb.append("MetaData field added and field id is : ").append(categoryMetaDataField1.getId());
            }
        return sb.toString();
    }

    public MappingJacksonValue getMetaDataFields(){

            List<CategoryMetaDataField> categoryMetaDataFields = categoryMetaDataFieldRepo.findAll();

            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

            FilterProvider filterProvider = new SimpleFilterProvider().addFilter("MetaData-Filter", filter);

            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(categoryMetaDataFields);

            mappingJacksonValue.setFilters(filterProvider);

            return mappingJacksonValue;
    }

    public String addCategory(ProductCategoryDto productCategoryDto){

        StringBuilder sb = new StringBuilder();
            ProductCategory productCategory = productCategoryRepo.findByCategoryName(productCategoryDto.getCategoryName());
            if(productCategory != null){
                sb.append("Category name already exist");
            }else {
                ProductCategory productCategory1 = new ProductCategory();
                BeanUtils.copyProperties(productCategoryDto,productCategory1);

                productCategoryRepo.save(productCategory1);

                sb.append("Category field added and category id is : ").append(productCategory1.getId());
            }
        return sb.toString();
    }

    public ProductCategory getCategoryById(Long categoryId){

            Optional<ProductCategory> productCategory = productCategoryRepo.findById(categoryId);

            if(productCategory.isPresent()){
                return productCategory.get();
            }else {
                throw new UserNotFoundException("Category not found");
            }
    }

    public List<ProductCategory> getCategory(){
            return productCategoryRepo.findAll();
    }

    public String updateCategory(Long categoryId, ProductCategoryDto productCategoryDto){

            Optional<ProductCategory> productCategory = productCategoryRepo.findById(categoryId);

            ProductCategory productCategory1 = productCategoryRepo.findByCategoryName(productCategoryDto.getCategoryName());
            if(productCategory.isPresent()){
                    productCategory.get().setCategoryName(productCategoryDto.getCategoryName());
                    //productCategory.get().setSubCategories(productCategoryDto.getSubCategories());
                    productCategoryRepo.save(productCategory.get());
            }else {
                throw new UserNotFoundException("Product category not found");
            }

        return "Category added";
    }

    public String addValue(Long categoryId,List<MetaDataFieldValueDto> metaDataFieldValueDtos){

        Optional<ProductCategory> productCategory = productCategoryRepo.findById(categoryId);

        if(productCategory.isPresent()){
            for(MetaDataFieldValueDto dto : metaDataFieldValueDtos ){
                Optional<CategoryMetaDataField> metaDataField = categoryMetaDataFieldRepo.findById(dto.getFieldId());

                if(metaDataField.isPresent()){
                    CategoryMetaDataFieldValues categoryMetaDataFieldValues=new CategoryMetaDataFieldValues();

                    Utility.checkDuplicates(dto.getValue());

                    categoryMetaDataFieldValues.setCategoryId(categoryId);
                    categoryMetaDataFieldValues.setFieldId(dto.getFieldId());
                    categoryMetaDataFieldValues.setValue(dto.getValue());

                    categoryMetaDataFieldValuesRepo.save(categoryMetaDataFieldValues);
                }else {
                    throw new UserNotFoundException("Field id does not exist");
                }

            }
        }else {
            throw new UserNotFoundException("Category id not exist");
        }

        return "Value added";
    }

    public String updateValue(MetaDataFieldValueDto metaDataFieldValueDto){

        Optional<ProductCategory> productCategory = productCategoryRepo.findById(metaDataFieldValueDto.getCategoryId());

        if(productCategory.isPresent()){

                Optional<CategoryMetaDataField> metaDataField = categoryMetaDataFieldRepo.findById(metaDataFieldValueDto.getFieldId());

                if(metaDataField.isPresent()){

                    Utility.checkDuplicates(metaDataFieldValueDto.getValue());
                    Optional<CategoryMetaDataFieldValues> valuesExist = categoryMetaDataFieldValuesRepo.findById(metaDataFieldValueDto.getId());

                    if(valuesExist.isPresent()) {
                        valuesExist.get().setValue(metaDataFieldValueDto.getValue());
                        categoryMetaDataFieldValuesRepo.save(valuesExist.get());
                    }else {
                        throw new BadRequestException("Value does not exist");
                    }
                }else {
                    throw new UserNotFoundException("Field id does not exist");
                }
        }else {
            throw new UserNotFoundException("Category id not exist");
        }

        return "Value updated";
    }

    //List of categories
    public List<CategoryProjectionDto> getCategories(){
        return productCategoryRepo.findAllCategories();
    }
}
