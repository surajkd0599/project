package com.ttn.bootcamp.project.ecommerce.controllers;

import com.ttn.bootcamp.project.ecommerce.models.ImageData;
import com.ttn.bootcamp.project.ecommerce.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(path = "/{userId}")
    public String uploadUserImage(@PathVariable(value = "userId") Long userId, @RequestParam("image") MultipartFile image) throws IOException {
        return imageService.uploadImage(userId, image);
    }

    @PostMapping(path = "/{userId}/variation/{variationId}")
    public String uploadVariationImage(@PathVariable(value = "userId") Long userId, @PathVariable(value = "variationId") Long variationId, @RequestParam("image") MultipartFile image) throws IOException {
        return imageService.uploadImage(userId, variationId, image);
    }


    @GetMapping(path = "/user/{userId}")
    public ResponseEntity<ByteArrayResource> getProfileImage(@PathVariable(value = "userId") Long userId) {
        ImageData imageData = imageService.downloadImage(userId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageData.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageData.getFileName() + "\"")
                .body(new ByteArrayResource(imageData.getProfileImage()));
    }

    @GetMapping(path = "/variation/{variationId}")
    public ResponseEntity<ByteArrayResource> getVariationPrimaryImage(@PathVariable(value = "variationId") Long variationId) {
        ImageData imageData = imageService.downloadImage1(variationId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageData.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageData.getFileName() + "\"")
                .body(new ByteArrayResource(imageData.getPrimaryImage()));
    }
}
