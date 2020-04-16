package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.CategoryMetaDataField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CategoryMetaDataFieldRepo extends JpaRepository<CategoryMetaDataField,Long> {

    CategoryMetaDataField findByName(String name);

}
