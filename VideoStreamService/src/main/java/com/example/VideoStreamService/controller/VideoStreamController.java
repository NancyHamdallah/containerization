package com.example.VideoStreamService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@RequestMapping("/stream")
public class VideoStreamController {

    private final String metadataServiceUrl = "http://my-sqldb-service:8082/video/getMetadata?filename="; // MySQL DB Service
    private final String fileSystemServiceUrl = "http://file-system-service:8081/storage/getFile?filename="; // File System Service
    private final String authServiceUrl = "http://auth-service:8086/auth/validate";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/video", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> streamVideo(@RequestParam String filename, @RequestHeader("Authorization") String authHeader) throws IOException {
        if (!isValidUser(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication");
        }
        ResponseEntity<String> metadataResponse = restTemplate.getForEntity(metadataServiceUrl + filename, String.class);
        if (metadataResponse.getStatusCode() != HttpStatus.OK || metadataResponse.getBody() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String videoUrl = metadataResponse.getBody();
        System.out.println("print videoUrl");
        ResponseEntity<byte[]> videoResponse = restTemplate.getForEntity(fileSystemServiceUrl + filename, byte[].class);
        if (videoResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("testActionWorkFLow");
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/mp4"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename= " + filename)
                    .body(videoResponse.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
}
