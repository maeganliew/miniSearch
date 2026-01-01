package com.minisearch.dto;

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

    // Convert DTO to Video entity
    public Video toVideo() {
        Video video = new Video();
        video.setTitle(this.title);
        video.setDescription(this.description);
        video.setTags(this.tags);
        return video;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
