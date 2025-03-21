package com.example.FileSystemS3Service.controller;

import com.example.FileSystemS3Service.service.FileUploadService;
import com.example.FileSystemS3Service.service.GetFileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/storage")

public class FileUploadController {
    private final FileUploadService fileUploadService;
    private final GetFileService getFileService;

    public FileUploadController(FileUploadService fileUploadService, GetFileService getFileService) {
        this.fileUploadService = fileUploadService;
        this.getFileService = getFileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file){

        return fileUploadService.save(file);
    }

    @GetMapping("/getFile")
    public ResponseEntity<Resource> getFile(@RequestParam String filename) {

        return getFileService.getFile(filename);

    }
}


