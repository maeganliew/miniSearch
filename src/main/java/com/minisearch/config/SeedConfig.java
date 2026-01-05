package com.minisearch.config;

import com.minisearch.model.Video;
import com.minisearch.repository.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedVideos(VideoRepository videoRepository) {
        return args -> {

            long count = videoRepository.count();
            if (count > 0) {
                System.out.println("Skipping seeding — videos already exist: " + count);
                return;
            }

            System.out.println("Seeding 10,000 videos…");

            List<Video> buffer = new ArrayList<>();
            Random rand = new Random();

            List<String> tagPool = Arrays.asList(
                    "travel", "food", "java", "spring", "coding", "tutorial",
                    "music", "funny", "cat", "dog", "ai", "machine learning",
                    "vlog", "review", "study", "fitness", "news"
            );

            int total = 10000;

            for (int i = 1; i <= total; i++) {

                String title = "Video #" + i + " — " +
                        (i % 2 == 0 ? "Java Tutorial" : "Travel Vlog");

                String description = "This is a sample description for video #" + i +
                        ". It contains some random keywords like java, spring, vlog, and coding.";

                List<String> tags = Arrays.asList(
                        tagPool.get(rand.nextInt(tagPool.size())),
                        tagPool.get(rand.nextInt(tagPool.size()))
                );

                Video v = new Video(
                        title,
                        description,
                        tags,
                        (long) rand.nextInt(500),
                        LocalDateTime.now().minusDays(rand.nextInt(500))
                );

                v.setViews((long) rand.nextInt(100000));
                v.setLikes((long) rand.nextInt(5000));

                buffer.add(v);

                // Save in batches of 500 for performance
                if (buffer.size() == 500) {
                    videoRepository.saveAll(buffer);
                    buffer.clear();
                }
            }

            if (!buffer.isEmpty()) {
                videoRepository.saveAll(buffer);
            }

            System.out.println("Done seeding 10,000 videos 🎉");
        };
    }
}
