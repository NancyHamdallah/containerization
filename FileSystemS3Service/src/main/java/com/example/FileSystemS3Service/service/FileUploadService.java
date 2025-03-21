package com.example.FileSystemS3Service.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    ResponseEntity<String> save(MultipartFile file);
}
