package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.CategoryMetaDataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetaDataFieldValuesRepo extends JpaRepository<CategoryMetaDataFieldValues,Long> {
}
