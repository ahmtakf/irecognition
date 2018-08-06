package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    List<ImageEntity> getAllImages();
    ImageEntity getImageById(long imageId);
    long addImage(MultipartFile file, long personId);
    ImageEntity updateImage(MultipartFile file, long imageId);
    void deleteImage(long imageId);
    int getNextImageNumber(long personId);

}
