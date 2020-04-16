package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.dtos.CategoryProjectionDto;
import com.ttn.bootcamp.project.ecommerce.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductCategory,Long> {

    ProductCategory findByCategoryName(String categoryName);

    @Query(value = "select p.id as categoryId,cv.id as valueId,cf.id as fieldId,p.category_name as categoryName,name,value" +
            " from category_meta_data_field_values cv inner join category_meta_data_field cf on cf.id=cv.category_metadata_field_id " +
            "inner join product_category p on p.id=cv.category_id",nativeQuery = true)
    List<CategoryProjectionDto> findAllCategories();
}
