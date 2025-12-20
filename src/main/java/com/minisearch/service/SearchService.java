package com.minisearch.service;

import com.minisearch.model.Video;
import com.minisearch.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SearchService {
    public final VideoRepository videoRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public SearchService(VideoRepository videoRepository, RedisTemplate<String, String> redisTemplate) {
        this.videoRepository = videoRepository;
        this.redisTemplate = redisTemplate;
    }

    public List<Video> searchVideo(String keyword, String sortBy) {
        // Check cache first
        String cacheKey = "search:" + keyword + (sortBy != null ? ":sort=" + sortBy : "");
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String cachedJson = ops.get(cacheKey);
        if (cachedJson != null) {
            System.out.println("Cache hit for key: " + cacheKey);
            return objectMapper.readValue(cachedJson, new TypeReference<List<Video>>() {});
        }

        // If not in cache, search from DB
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

        // Save to cache, time to live 10 minutes
        // ops.set(key, value, ttl)
        String json = objectMapper.writeValueAsString(videos);
        ops.set(cacheKey, json, Duration.ofMinutes(10));
        System.out.println("Cache miss. Stored key: " + cacheKey);
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
