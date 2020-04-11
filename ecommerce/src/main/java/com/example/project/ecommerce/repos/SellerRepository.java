package com.example.project.ecommerce.repos;

import com.example.project.ecommerce.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
    Page<Seller> findAll(Pageable pageable);

    Seller findByUserId(Long userId);
    Seller findByGst(String gst);
}
