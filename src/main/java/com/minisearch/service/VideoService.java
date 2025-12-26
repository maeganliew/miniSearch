package com.minisearch.service;

import com.minisearch.exception.VideoNotFoundException;
import com.minisearch.model.Video;
import com.minisearch.repository.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

//handle business logic
@Service //makes the class below available as a bean
public class VideoService {

    private final VideoRepository videoRepository;
    private final AsyncService asyncService;

    public VideoService(VideoRepository videoRepository, AsyncService asyncService) {
        this.videoRepository = videoRepository;
        this.asyncService = asyncService;
    }

    public Page<Video> getAllVideos(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    public Video getSingleVideo(Long id) {
        return videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException(id));
    }

    public Video addVideo(Video video) {
        if (video.getTitle() == null || video.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (video.getUploaderId() == null) {
            throw new IllegalArgumentException("UploaderId is required");
        }
        if (video.getUploadDate() == null) {
            video.setUploadDate(LocalDateTime.now());
        }
        // telling JPA that this is a video that needs to be inserted, not updated. in case client included id in body accidentally
        video.setId(null);
        video.setThumbnailUrl("https://example.com/thumbnails/placeholder.jpg");
        Video savedVideo = videoRepository.save(video);
        asyncService.generateThumbnail(savedVideo.getId());
        return videoRepository.save(savedVideo);
    }

    public Video updateVideo(Video video, Long id) {
        Video existing = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException(id));

        if (video.getTitle() != null && !video.getTitle().isBlank()) {
            existing.setTitle(video.getTitle());
        }
        if (video.getDescription() != null) {
            existing.setDescription(video.getDescription());
        }
        if (video.getTags() != null) {
            existing.setTags(video.getTags());
        }
        return videoRepository.save(existing);
    }

    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }

}
