package com.example.FileSystemS3Service.service.impl;

import com.example.FileSystemS3Service.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.path}")
    private String filePath;
    @Override
    public ResponseEntity<String> save(MultipartFile file) {
        // Use relative path inside Docker container, for example "/uploads"
        String dir = File.separator + filePath;
        try {
            // Create the file path for saving the file in the container's mounted volume
            File newFile = new File(dir + File.separator + file.getOriginalFilename());
            // Transfer the file to the new location
            file.transferTo(newFile);
            return ResponseEntity.ok(newFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

