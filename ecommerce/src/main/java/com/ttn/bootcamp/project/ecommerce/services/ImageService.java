package com.ttn.bootcamp.project.ecommerce.services;

import com.ttn.bootcamp.project.ecommerce.exceptions.BadRequestException;
import com.ttn.bootcamp.project.ecommerce.exceptions.NotFoundException;
import com.ttn.bootcamp.project.ecommerce.models.ImageData;
import com.ttn.bootcamp.project.ecommerce.repos.ImageRepo;
import com.ttn.bootcamp.project.ecommerce.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserRepo userRepo;

    public String uploadImage(Long userId, MultipartFile image) throws IOException {

        boolean isValid = (image.getContentType().equals("image/png") || image.getContentType().equals("image/jpeg")
                || image.getContentType().equals("image/jpg") || image.getContentType().equals("image/bmp"));
        if (!isValid) {
            throw new BadRequestException("Image uploaded should be {.png, .bmp,.jpeg,.jpg}");
        }
        ImageData userImageData = new ImageData();

        System.out.println("Image uploaded is " + image.getContentType());
        userImageData.setProfileImage(image.getBytes());
        userImageData.setContentType(image.getContentType());
        userImageData.setFileName(StringUtils.cleanPath(image.getOriginalFilename()));
        userImageData.setUserId(userId);
        imageRepo.save(userImageData);

        return "Image uploaded";
    }

    public String uploadImage(Long userId, Long variationId, MultipartFile image) throws IOException {

        boolean isValid = (image.getContentType().equals("image/png") || image.getContentType().equals("image/jpeg")
                || image.getContentType().equals("image/jpg") || image.getContentType().equals("image/bmp"));
        if (!isValid) {
            throw new BadRequestException("Image uploaded should be {.png, .bmp,.jpeg,.jpg}");
        }
        ImageData imageData = new ImageData();

        System.out.println("Image uploaded is " + image.getContentType());
        imageData.setPrimaryImage(image.getBytes());
        imageData.setContentType(image.getContentType());
        imageData.setFileName(StringUtils.cleanPath(image.getOriginalFilename()));
        imageData.setUserId(userId);
        imageData.setProductVariationId(variationId);
        imageRepo.save(imageData);

        return "Image uploaded";
    }

    public ImageData downloadImage(Long userId) {
        ImageData userImage = imageRepo.findImageByUserId(userId);

        if (null != userImage) {
            return userImage;
        } else {
            throw new NotFoundException("File not found with id " + userId);
        }
    }

    public ImageData downloadImage1(Long variationId) {
        ImageData userImage = imageRepo.findImageByVariationId(variationId);

        if (null != userImage) {
            return userImage;
        } else {
            throw new NotFoundException("File not found with id " + variationId);
        }
    }
}
