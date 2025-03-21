package com.example.MySQLDBService.repository;

import com.example.MySQLDBService.dto.VideoMetadata;
import com.example.MySQLDBService.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {
    VideoEntity findByFileName(String fileName);
}
