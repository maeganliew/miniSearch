package com.minisearch.controller;

import com.minisearch.dto.VideoRequest;
import com.minisearch.dto.VideoResponse;
import com.minisearch.model.Video;
import com.minisearch.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/mini-search/video")
@Tag(name = "Videos", description = "CRUD operations for video metadata")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // Get video, with optional params. If no params, default getAllVideos (Pagination uses Pageable Argument Resolver)
    // Spring automatically resolves query parameters into a Pageable object.
    @Operation(summary = "Get all videos", description = "Retrieve all videos with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved videos")
    })
    @GetMapping
    public Page<VideoResponse> getVideos(Pageable pageable) {
        Page<Video> page =  videoService.getAllVideos(pageable);
        return page.map(video -> VideoResponse.from(video));
    }

    // Get single video by id
    @Operation(summary = "Get single video by ID", description = "Retrieve a video by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video found"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @GetMapping("/{id}")
    public VideoResponse getSingleVideo(@PathVariable Long id) {
        return videoService.getSingleVideo(id);
    }

    // Add new video
    @Operation(summary = "Add new video", description = "Create a new video entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Video created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public VideoResponse addVideo(@Valid @RequestBody VideoRequest videoRequest) {
        Video video = videoRequest.toVideo();
        Video saved = videoService.addVideo(video);
        return VideoResponse.from(saved);
    }

    // Update video
    @Operation(summary = "Update existing video", description = "Update video metadata by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @PutMapping("/{id}")
    public VideoResponse updateVideo(@Valid @RequestBody VideoRequest videoRequest, @PathVariable Long id) {
        Video video = videoRequest.toVideo();
        return videoService.updateVideo(video, id);
    }

    // Delete video
    @Operation(summary = "Delete video", description = "Delete a video by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Video deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }
}
