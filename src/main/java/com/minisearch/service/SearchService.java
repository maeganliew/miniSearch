package com.minisearch.service;

import com.minisearch.model.Video;
import com.minisearch.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SearchService {
    public final VideoRepository videoRepository;

    public SearchService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> searchVideo(String keyword, String sortBy) {
        List<Video> videos = videoRepository.searchBasic(keyword);
        if (videos == null) return List.of();

        // If sort param is provided, sort it via sort param
        if (sortBy != null) {
            videos = videos.stream().sorted((a, b) -> switch(sortBy.toLowerCase()) {
                case "likes" -> b.getLikes().compareTo(a.getLikes());
                case "views" -> b.getViews().compareTo(a.getViews());
                case "date" -> b.getUploadDate().compareTo(a.getUploadDate());
                default -> score(b, keyword) - score(a, keyword);
                }
            ).toList();
        } else {
            // default = sort by relevance
            videos = videos.stream()
                    .sorted((a, b) -> score(b, keyword) - score(a, keyword))
                    .toList();
        }
        return videos;
    }

    private int score(Video video, String keyword) {
        int score = 0;
        String q = keyword.toLowerCase();

        // Title match +3 pts
        if (video.getTitle() != null && video.getTitle().toLowerCase().contains(q)) {
            score += 3;
        }
        // Tags match +2 pts, Stream is a pipeline of elements
        if (video.getTags() != null &&
                video.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(q))) {
            score += 2;
        }
        // Description match +1 pts
        if (video.getDescription() != null &&
                video.getDescription().toLowerCase().contains(q)) {
            score += 1;
        }
        return score;
    }
}
