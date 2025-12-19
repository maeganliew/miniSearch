package com.minisearch.controller;

import com.minisearch.model.Video;
import com.minisearch.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Page<Video> getVideos(Pageable pageable) {
        return videoService.getAllVideos(pageable);
    }

    // Get single video by id
    @GetMapping("/{id}")
    public Video getSingleVideo(@PathVariable Long id) {
        return videoService.getSingleVideo(id);
    }

    // Add new video
    @PostMapping
    public Video addVideo(@RequestBody Video video) {
        return videoService.addVideo(video);
    }

    // Update video
    @PutMapping("/{id}")
    public Video updateVideo(@RequestBody Video video, @PathVariable Long id) {
        return videoService.updateVideo(video, id);
    }

    // Delete video
    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}
