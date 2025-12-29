package com.minisearch.controller;

import com.minisearch.dto.VideoRequest;
import com.minisearch.dto.VideoResponse;
import com.minisearch.model.Video;
import com.minisearch.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/mini-search/video")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // Get video, with optional params. If no params, default getAllVideos (Pagination uses Pageable Argument Resolver)
    // Spring automatically resolves query parameters into a Pageable object.
    @GetMapping
    public Page<VideoResponse> getVideos(Pageable pageable) {
        Page<Video> page =  videoService.getAllVideos(pageable);
        return page.map(video -> VideoResponse.from(video));
    }

    // Get single video by id
    @GetMapping("/{id}")
    public VideoResponse getSingleVideo(@PathVariable Long id) {
        Video video = videoService.getSingleVideo(id);
        return VideoResponse.from(video);
    }

    // Add new video
    @PostMapping
    public VideoResponse addVideo(@Valid @RequestBody VideoRequest videoRequest) {
        Video video = videoRequest.toVideo();
        Video saved = videoService.addVideo(video);
        return VideoResponse.from(saved);
    }

    // Update video
    @PutMapping("/{id}")
    public VideoResponse updateVideo(@Valid @RequestBody VideoRequest videoRequest, @PathVariable Long id) {
        Video video = videoRequest.toVideo();
        Video updated = videoService.updateVideo(video, id);
        return VideoResponse.from(video);
    }

    // Delete video
    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}
