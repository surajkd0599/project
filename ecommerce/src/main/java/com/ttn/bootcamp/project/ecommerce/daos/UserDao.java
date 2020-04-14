package com.ttn.bootcamp.project.ecommerce.daos;

import com.ttn.bootcamp.project.ecommerce.exceptions.UserNotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.AppUser;
import com.ttn.bootcamp.project.ecommerce.models.GrantedAuthorityImpl;
import com.ttn.bootcamp.project.ecommerce.models.Role;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {
    @Autowired
    UserRepository userRepository;

    public AppUser loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        System.out.println(user.getPassword());

        List<GrantedAuthorityImpl> grantedAuthorityImpl = new ArrayList<>();
        System.out.println(user);
        if(user.isActive()) {
            if (username != null) {
                for (Role auth : user.getRoles()) {
                    grantedAuthorityImpl.add(new GrantedAuthorityImpl(auth.getRole()));
                }
                return new AppUser(user.getEmail(), user.getPassword(),
                        grantedAuthorityImpl, !user.isEnabled(), !user.isCredentialsNonExpired(), user.isAccountNonLocked());
            } else {
                throw new UserNotFoundException("User not found");
            }
        }else {
            throw new RuntimeException("Account is not activated.");
        }
    }
}
