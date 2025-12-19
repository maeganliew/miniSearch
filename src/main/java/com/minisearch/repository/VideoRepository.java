package com.minisearch.repository;

import com.minisearch.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// VideoRepository knows which table to query because it is typed with the Video entity
// the Video entity is mapped to the videos table via @Entity + @Table.
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("""
        SELECT DISTINCT v
        FROM Video v
        LEFT JOIN v.tags t
        WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(v.description) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(t) LIKE LOWER(CONCAT('%', :q, '%'))
    """)

    // @Param("q") links the method argument keyword to :q in the query
    // Spring/Hibernate automatically converts the JPQL result to Video objects
    List<Video> searchBasic(@Param("q") String keyword);

    // Query 2
    // Function 2
}
