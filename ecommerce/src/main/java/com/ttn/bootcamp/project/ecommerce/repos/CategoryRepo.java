package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    @Query("from Category where categoryName=:categoryName AND parentId=:parentId")
    Category findExistingCategory(@Param("categoryName") String categoryName, @Param("parentId") Long parentId);

    @Query("from Category where categoryName=:categoryName AND parentId=:parentId AND id=:categoryId")
    Category findExistingCategory(@Param("categoryName") String categoryName, @Param("parentId") Long parentId, @Param("categoryId") Long categoryId);

    @Query("from Category where categoryName=:categoryName AND id=:parentId")
    Category findExistingCategory1(@Param("categoryName") String categoryName, @Param("parentId") Long parentId);

    @Query("from Category where parentId=0")
    List<Category> findId();

    @Query("from Category where parent_id=:parentId")
    Set<Category> findByParentId(@Param("parentId") Long parentId);

    @Query("from Category where id=:categoryId OR parentId=:categoryId")
    List<Category> findExistingCategory(@Param("categoryId") Long categoryId);

    Category findByCategoryName(String categoryName);

}
