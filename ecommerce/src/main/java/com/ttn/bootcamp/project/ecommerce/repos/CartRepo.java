package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
}
