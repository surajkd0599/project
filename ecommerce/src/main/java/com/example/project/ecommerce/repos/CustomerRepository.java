package com.example.project.ecommerce.repos;

import com.example.project.ecommerce.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Page<Customer> findAll(Pageable pageable);

    Customer findByUserId(Long userId);

    Customer findByEmail(String email);
}
