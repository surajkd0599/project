package com.ttn.bootcamp.project.ecommerce.repos;

import com.ttn.bootcamp.project.ecommerce.models.UserAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttemptRepo extends JpaRepository<UserAttempts, Long> {
    UserAttempts findByUsername(String username);

    void deleteByUsername(String username);
}
