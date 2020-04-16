package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.User;
import com.ttn.bootcamp.project.ecommerce.models.UserImage;
import com.ttn.bootcamp.project.ecommerce.repos.UserImageRepo;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserImageService {

    @Autowired
    private UserImageRepo userImageRepo;

    @Autowired
    private UserRepo userRepo;

    public String uploadImage(Long userId, MultipartFile image) throws IOException {

        Optional<User> user = userRepo.findById(userId);

        UserImage userImage = new UserImage();

        userImage.setImage(image.getBytes());

        userImage.setUserId(userId);

        userImageRepo.save(userImage);

        return "Image uploaded";
    }

    public UserImage getFile(Long fileId) {
        return userImageRepo.findById(fileId)
                .orElseThrow(() -> new NotFoundException("File not found with id " + fileId));
    }
}
