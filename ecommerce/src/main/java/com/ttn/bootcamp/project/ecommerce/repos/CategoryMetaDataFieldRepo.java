package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.CategoryMetaDataField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetaDataFieldRepo extends JpaRepository<CategoryMetaDataField, Long> {

    CategoryMetaDataField findByName(String name);

    Page<CategoryMetaDataField> findAll(Pageable pageable);

}
