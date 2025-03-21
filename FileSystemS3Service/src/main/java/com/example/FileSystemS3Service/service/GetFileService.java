package com.example.FileSystemS3Service.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface GetFileService {

    ResponseEntity<Resource> getFile(String filename);
}
