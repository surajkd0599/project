package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.models.UserImage;
import com.ttn.bootcamp.project.ecommerce.services.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @PostMapping(path = "{userId}/image")
    public String uploadImage(@PathVariable(value = "userId") Long userId, @RequestParam("image") MultipartFile image) throws IOException {
        return userImageService.uploadImage(userId,image);
    }

    @GetMapping(path = "{userId}/image/{imageId}")
    public UserImage getImage(@PathVariable(value = "imageId") Long imageId){
        return userImageService.getFile(imageId);
    }
}
