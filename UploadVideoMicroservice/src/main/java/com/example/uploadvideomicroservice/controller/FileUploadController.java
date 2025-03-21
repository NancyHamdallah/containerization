package com.example.uploadvideomicroservice.controller;


import com.example.uploadvideomicroservice.dto.VideoMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    private final String fileSystemServiceUrl = "http://file-system-service:8081/storage/upload";
    private final String mySQLServiceUrl = "http://my-sqldb-service:8082/video/save";
    private final String authServiceUrl = "http://auth-service:8086/auth/validate";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file, @RequestHeader("Authorization") String authHeader) throws IOException {
        if (!isValidUser(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication");
        }
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(convertMultipartFileToFile(file)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        // Send request using RestTemplate
        ResponseEntity<String> response = restTemplate.postForEntity(fileSystemServiceUrl, request, String.class);
        String filePath = response.getBody();
        // Save the metadata in MySQL Service
        VideoMetadata videoMetadata = new VideoMetadata(file.getOriginalFilename(), filePath);
        HttpEntity<VideoMetadata> metadataRequest = new HttpEntity<>(videoMetadata);
        restTemplate.postForEntity(mySQLServiceUrl, metadataRequest, String.class);
        return ResponseEntity.ok("File uploaded successfully!");

    }

    private boolean isValidUser(String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(authServiceUrl, HttpMethod.GET, requestEntity, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

}
