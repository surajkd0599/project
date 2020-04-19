package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerActivateRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByUserEmail(String email);

    VerificationToken findByToken(String token);

    void deleteByUserEmail(String email);
}
