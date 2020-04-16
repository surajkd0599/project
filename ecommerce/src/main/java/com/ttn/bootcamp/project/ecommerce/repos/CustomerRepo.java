package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
    Page<Customer> findAll(Pageable pageable);

    Optional<Customer> findById(Long id);

    Customer findByUsername(String username);

    Customer findByEmail(String email);
}
