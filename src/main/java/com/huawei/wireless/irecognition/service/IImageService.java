package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import org.springframework.core.io.Resource;
import java.util.Map;

public interface IImageService {

    List<ImageEntity> getAllImages();

    ImageEntity getImageById(long imageId);

    long addImage(MultipartFile file, long personId);

    ImageEntity updateImage(MultipartFile file, long imageId);

    void deleteImage(long imageId);

    ImageEntity checkImage(MultipartFile file);

    Resource getImageByURL(String url);

    DataTablesOutput<ImageEntity> getPage(DataTablesInput input);
}


