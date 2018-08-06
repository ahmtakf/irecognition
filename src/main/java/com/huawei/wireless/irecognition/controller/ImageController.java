package com.huawei.wireless.irecognition.controller;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import com.huawei.wireless.irecognition.repository.ImageRepository;
import com.huawei.wireless.irecognition.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("image")
public class ImageController {

    @Autowired
    private IImageService imageService;

    private Map<String, ImageEntity> images = new HashMap<>();

    @GetMapping("{id}")
    public ResponseEntity<ImageEntity> getImageById(@PathVariable("id") long id) {
        ImageEntity image = imageService.getImageById(id);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
    @GetMapping("images")
    public ResponseEntity<List<ImageEntity>> getAllImages() {
        List<ImageEntity> list = imageService.getAllImages();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Long> addImage(@RequestParam("image") MultipartFile file, @RequestParam("personId") String personId) {

        long id = imageService.addImage(file, Long.parseLong(personId));

        if ( id == 0)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        return new ResponseEntity<>( id, HttpStatus.OK);

    }

    @PutMapping("update")
    public ResponseEntity<ImageEntity> updateImage(@RequestParam("image") MultipartFile file, @RequestParam("personId") long personId) {

        return new ResponseEntity<>( imageService.updateImage(file, personId), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") Integer id) {
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

