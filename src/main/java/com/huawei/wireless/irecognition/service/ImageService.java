package com.huawei.wireless.irecognition.service;

import com.huawei.wireless.irecognition.entity.ImageEntity;
import com.huawei.wireless.irecognition.entity.PersonEntity;
import com.huawei.wireless.irecognition.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService{

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<ImageEntity> getAllImages() {
        List<ImageEntity> list = new ArrayList<>();
        imageRepository.findAll().forEach(e -> {
            list.add(e);});

        return list;
    }

    @Override
    public ImageEntity getImageById(long imageId) {
        return imageRepository.findOne(imageId);
    }

    @Override
    public long addImage(MultipartFile file, long personId) {


        ImageEntity imageEntity = new ImageEntity();

        String imageName = file.getOriginalFilename();
        if (imageName.substring(imageName.length() - 3, imageName.length()).equals("jpg"))
            imageEntity.setUrl( personId + "_" + getNextImageNumber(personId) + ".jpg");
        else if (imageName.substring(imageName.length() - 4, imageName.length()).equals("jpeg"))
            imageEntity.setUrl( personId + "_" + getNextImageNumber(personId) + ".jpeg");
        else if (imageName.substring(imageName.length() - 3, imageName.length()).equals("png"))
            imageEntity.setUrl( personId + "_" + getNextImageNumber(personId) + ".png");

        try {
            storageService.store(file, imageEntity.getUrl());
            System.out.println( "You successfully uploaded " + file.getOriginalFilename() + "!");
        } catch (Exception e) {
            System.out.println( "FAIL to upload " + file.getOriginalFilename() + "!");
        }
        //After face recognition process save the training
        //imageEntity.setRecognizer("haha");
        imageEntity.setPerson(new PersonEntity(personId));

        return imageRepository.save(imageEntity).getId();
    }

    @Override
    public ImageEntity updateImage(MultipartFile file, long imageId) {
        ImageEntity imageEntity = getImageById(imageId);

        try {
            storageService.store(file, imageEntity.getUrl());
            System.out.println( "You successfully uploaded " + file.getOriginalFilename() + "!");
        } catch (Exception e) {
            System.out.println( "FAIL to upload " + file.getOriginalFilename() + "!");
        }
        //After face recognition process save the training
        //imageEntity.setRecognizer("haha");
        imageEntity.setPerson(imageEntity.getPerson());

        return imageRepository.save(imageEntity);
    }


    @Override
    public void deleteImage(long imageId) {
        imageRepository.delete(imageId);
    }

    @Override
    public int getNextImageNumber(long personId) {
        List<ImageEntity> images = imageRepository.findImageEntitiesByPersonId(personId);
        return images.size() + 1;
    }

}
