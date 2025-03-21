package com.example.MySQLDBService.dto;
public class VideoMetadata {
    private String fileName;
    private String filePath;

    public VideoMetadata(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }
    public String getVideoUrl() {
        return this.filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}