package com.huawei.wireless.irecognition.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Service
public class StorageService {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private static final Path rootLocation = Paths.get("upload-dir");

    public void store(MultipartFile file, String filename) {
        try {
            Files.copy(file.getInputStream(), rootLocation.resolve( filename));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public MultipartFile loadFile(String filename) {
        try {
            Path path = rootLocation.resolve(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, resource.getFile().getName(), (int) resource.getFile().length() , resource.getFile().getParentFile());
                fileItem.getOutputStream();
                return new CommonsMultipartFile(fileItem);
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (IOException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
