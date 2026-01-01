package com.minisearch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minisearch.model.Video;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

// what clients are required to send when making requests
public class VideoRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private List<String> tags;

    private Long uploaderId;

    // will throw InvalidFormatException if the date passed in doesnt follow this format
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadDate; // optional, can default to now in service

    // Convert DTO to Video entity
    public Video toVideo() {
        Video video = new Video();
        video.setTitle(this.title);
        video.setDescription(this.description);
        video.setTags(this.tags);
        video.setUploaderId(this.uploaderId);
        video.setUploadDate(this.uploadDate != null ? this.uploadDate : LocalDateTime.now());
        return video;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Long getUploaderId() { return uploaderId; }
    public void setUploaderId(Long uploaderId) { this.uploaderId = uploaderId; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
