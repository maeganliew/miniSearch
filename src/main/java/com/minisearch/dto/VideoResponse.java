package com.minisearch.dto;

import com.minisearch.model.Video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoResponse implements Serializable {

    private Long id;
    private String title;
    private String description;
    private List<String> tags;
    private Long uploaderId;
    private String thumbnailUrl;
    private Long views;
    private Long likes;

    // Converting video entity to DTO
    public static VideoResponse from(Video video) {
        VideoResponse dto = new VideoResponse();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setTags(video.getTags() != null ? new ArrayList<>(video.getTags()) : null);
        dto.setUploaderId(video.getUploaderId());
        dto.setThumbnailUrl(video.getThumbnailUrl());
        dto.setViews(video.getViews());
        dto.setLikes(video.getLikes());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Long getUploaderId() { return uploaderId; }
    public void setUploaderId(Long uploaderId) { this.uploaderId = uploaderId; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public Long getViews() { return views; }
    public void setViews(Long views) { this.views = views; }

    public Long getLikes() { return likes; }
    public void setLikes(Long likes) { this.likes = likes; }
}
