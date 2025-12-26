package com.minisearch.service;

import com.minisearch.exception.VideoNotFoundException;
import com.minisearch.model.Video;
import com.minisearch.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AsyncService {
    // up to 2 background worker threads created
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final VideoRepository videoRepository;

    public AsyncService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void generateThumbnail(Long videoId) {
        // thread manager, "run the code on a separate thread, not the current one"
        executor.submit(() -> {
            try {
                Thread.sleep(3000);
                Video video = videoRepository.findById(videoId).orElseThrow(() -> new VideoNotFoundException(videoId));
                video.setThumbnailUrl("https://example.com/thumbnails/" + videoId + ".jpg");
                videoRepository.save(video);
                System.out.println("Thumbnail updated for video " + videoId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
