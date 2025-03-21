package com.example.MySQLDBService.controller;

import com.example.MySQLDBService.dto.VideoMetadata;
import com.example.MySQLDBService.entity.VideoEntity;
import com.example.MySQLDBService.repository.VideoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/video")
public class VideoMetadataController {
    private final VideoRepository videoRepository;
    public VideoMetadataController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }
    @PostMapping("/save")
    public ResponseEntity<String> saveMetadata(@RequestBody VideoMetadata videoMetadata) {
        try {
            // Save the video metadata into the database
            videoRepository.save(new VideoEntity(videoMetadata.getFileName(), videoMetadata.getFilePath()));
           return ResponseEntity.ok("Video metadata saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving metadata");
        }
    }
    @GetMapping("/getMetadata")
    public ResponseEntity<String> getVideoMetadata(@RequestParam String filename) {
       if (filename == null || filename.isEmpty()) {
            return ResponseEntity.badRequest().body("Filename is required!");  // Returns 400
        }
        VideoEntity videoEntity = videoRepository.findByFileName(filename);
        if (videoEntity == null) {
            return ResponseEntity.notFound().build();
        }

        VideoMetadata metadata = new VideoMetadata(videoEntity.getFileName(), videoEntity.getFilePath());
        String videoUrl = metadata.getVideoUrl();
        return ResponseEntity.ok(videoUrl);


    }
}
