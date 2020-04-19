package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<ImageData, Long> {

    @Query("from ImageData where userId=:userId AND productVariationId is null")
    ImageData findImageByUserId(@Param("userId") Long userId);

    @Query("from ImageData where productVariationId=:variationId")
    ImageData findImageByVariationId(@Param("variationId") Long variationId);
}
