package com.huawei.wireless.irecognition.controller;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import com.huawei.wireless.irecognition.entity.PersonEntity;
import com.huawei.wireless.irecognition.repository.ImageRepository;
import com.huawei.wireless.irecognition.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @PostMapping("check")
    public ResponseEntity<ImageEntity> checkImage(@RequestParam("image") MultipartFile file) {

        return new ResponseEntity<>( imageService.checkImage(file), HttpStatus.OK);
    }

    @PostMapping("getimage")
    public ResponseEntity<Resource> getImageByURL(@RequestParam("url") String url, HttpServletRequest request) {

        Resource resource = imageService.getImageByURL(url);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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

